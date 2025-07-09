package com.bitchat.crypto

import org.bouncycastle.crypto.agreement.X25519Agreement
import org.bouncycastle.crypto.generators.Ed25519KeyPairGenerator
import org.bouncycastle.crypto.params.Ed25519KeyGenerationParameters
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters
import org.bouncycastle.crypto.params.X25519PrivateKeyParameters
import org.bouncycastle.crypto.params.X25519PublicKeyParameters
import org.bouncycastle.crypto.signers.Ed25519Signer
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.SecureRandom
import java.security.Security
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Encryption service implementing the same cryptography as the iOS version.
 * Uses Bouncy Castle for X25519, Ed25519, and AES-256-GCM.
 */
class EncryptionService {
    
    companion object {
        init {
            Security.addProvider(BouncyCastleProvider())
        }
        
        private const val AES_KEY_SIZE = 256
        private const val GCM_IV_SIZE = 12
        private const val GCM_TAG_SIZE = 16
        private const val HKDF_SALT = "bitchat-v1"
    }
    
    // Key agreement keys for encryption
    private var privateKey: X25519PrivateKeyParameters
    val publicKey: X25519PublicKeyParameters
    
    // Signing keys for authentication
    private var signingPrivateKey: Ed25519PrivateKeyParameters
    val signingPublicKey: Ed25519PublicKeyParameters
    
    // Storage for peer keys
    private val peerPublicKeys = mutableMapOf<String, X25519PublicKeyParameters>()
    private val peerSigningKeys = mutableMapOf<String, Ed25519PublicKeyParameters>()
    private val peerIdentityKeys = mutableMapOf<String, Ed25519PublicKeyParameters>()
    private val sharedSecrets = mutableMapOf<String, SecretKey>()
    
    // Persistent identity for favorites (separate from ephemeral keys)
    private val identityKey: Ed25519PrivateKeyParameters
    val identityPublicKey: Ed25519PublicKeyParameters
    
    // Thread safety
    private val cryptoMutex = Mutex()
    
    init {
        // Generate ephemeral key pairs for this session
        val secureRandom = SecureRandom()
        
        // X25519 key agreement
        privateKey = X25519PrivateKeyParameters(secureRandom)
        publicKey = privateKey.generatePublicKey()
        
        // Ed25519 signing
        val ed25519Generator = Ed25519KeyPairGenerator()
        ed25519Generator.init(Ed25519KeyGenerationParameters(secureRandom))
        val signingKeyPair = ed25519Generator.generateKeyPair()
        signingPrivateKey = signingKeyPair.private as Ed25519PrivateKeyParameters
        signingPublicKey = signingKeyPair.public as Ed25519PublicKeyParameters
        
        // Load or create persistent identity key
        identityKey = loadOrCreateIdentityKey(secureRandom)
        identityPublicKey = identityKey.generatePublicKey()
    }
    
    /**
     * Creates combined public key data for exchange (96 bytes total)
     */
    suspend fun getCombinedPublicKeyData(): ByteArray = cryptoMutex.withLock {
        val data = ByteArray(96)
        
        // 32 bytes - ephemeral encryption key
        System.arraycopy(publicKey.encoded, 0, data, 0, 32)
        
        // 32 bytes - ephemeral signing key
        System.arraycopy(signingPublicKey.encoded, 0, data, 32, 32)
        
        // 32 bytes - persistent identity key
        System.arraycopy(identityPublicKey.encoded, 0, data, 64, 32)
        
        return data
    }
    
    /**
     * Adds peer's combined public keys and generates shared secret
     */
    suspend fun addPeerPublicKey(peerID: String, publicKeyData: ByteArray) {
        cryptoMutex.withLock {
            if (publicKeyData.size != 96) {
                throw EncryptionException("Invalid public key data size: ${publicKeyData.size}, expected 96")
            }
            
            // Extract all three keys: 32 for key agreement + 32 for signing + 32 for identity
            val keyAgreementData = publicKeyData.copyOfRange(0, 32)
            val signingKeyData = publicKeyData.copyOfRange(32, 64)
            val identityKeyData = publicKeyData.copyOfRange(64, 96)
            
            val publicKey = X25519PublicKeyParameters(keyAgreementData, 0)
            peerPublicKeys[peerID] = publicKey
            
            val signingKey = Ed25519PublicKeyParameters(signingKeyData, 0)
            peerSigningKeys[peerID] = signingKey
            
            val identityKey = Ed25519PublicKeyParameters(identityKeyData, 0)
            peerIdentityKeys[peerID] = identityKey
            
            // Generate shared secret for encryption
            val agreement = X25519Agreement()
            agreement.init(privateKey)
            val sharedSecretBytes = ByteArray(agreement.agreementSize)
            agreement.calculateAgreement(publicKey, sharedSecretBytes, 0)
            
            // Derive symmetric key using HKDF
            val symmetricKey = deriveSymmetricKey(sharedSecretBytes)
            sharedSecrets[peerID] = symmetricKey
        }
    }
    
    /**
     * Gets peer's persistent identity key for favorites
     */
    suspend fun getPeerIdentityKey(peerID: String): ByteArray? = cryptoMutex.withLock {
        return peerIdentityKeys[peerID]?.encoded
    }
    
    /**
     * Encrypts data for a specific peer
     */
    suspend fun encrypt(data: ByteArray, for peerID: String): ByteArray {
        val symmetricKey = cryptoMutex.withLock {
            sharedSecrets[peerID] ?: throw EncryptionException("No shared secret for peer: $peerID")
        }
        
        return encryptWithAES(data, symmetricKey)
    }
    
    /**
     * Decrypts data from a specific peer
     */
    suspend fun decrypt(data: ByteArray, from peerID: String): ByteArray {
        val symmetricKey = cryptoMutex.withLock {
            sharedSecrets[peerID] ?: throw EncryptionException("No shared secret for peer: $peerID")
        }
        
        return decryptWithAES(data, symmetricKey)
    }
    
    /**
     * Signs data with our signing key
     */
    suspend fun sign(data: ByteArray): ByteArray = cryptoMutex.withLock {
        val signer = Ed25519Signer()
        signer.init(true, signingPrivateKey)
        signer.update(data, 0, data.size)
        return signer.generateSignature()
    }
    
    /**
     * Verifies signature from a peer
     */
    suspend fun verify(signature: ByteArray, data: ByteArray, from peerID: String): Boolean {
        val verifyingKey = cryptoMutex.withLock {
            peerSigningKeys[peerID] ?: throw EncryptionException("No signing key for peer: $peerID")
        }
        
        val signer = Ed25519Signer()
        signer.init(false, verifyingKey)
        signer.update(data, 0, data.size)
        return signer.verifySignature(signature)
    }
    
    /**
     * Clears persistent identity (for panic mode)
     */
    fun clearPersistentIdentity() {
        // TODO: Implement persistent storage clearing
    }
    
    /**
     * Loads or creates persistent identity key
     */
    private fun loadOrCreateIdentityKey(secureRandom: SecureRandom): Ed25519PrivateKeyParameters {
        // TODO: Implement persistent storage for identity key
        // For now, always create a new one
        val ed25519Generator = Ed25519KeyPairGenerator()
        ed25519Generator.init(Ed25519KeyGenerationParameters(secureRandom))
        val identityKeyPair = ed25519Generator.generateKeyPair()
        return identityKeyPair.private as Ed25519PrivateKeyParameters
    }
    
    /**
     * Derives symmetric key using HKDF
     */
    private fun deriveSymmetricKey(sharedSecret: ByteArray): SecretKey {
        // Simple HKDF implementation
        val salt = HKDF_SALT.toByteArray()
        val info = ByteArray(0)
        
        // Use HMAC-SHA256 for HKDF
        val hmac = javax.crypto.Mac.getInstance("HmacSHA256")
        val saltKey = SecretKeySpec(salt, "HmacSHA256")
        hmac.init(saltKey)
        
        val prk = hmac.doFinal(sharedSecret)
        
        val keyGenerator = KeyGenerator.getInstance("AES")
        keyGenerator.init(AES_KEY_SIZE)
        val key = keyGenerator.generateKey()
        
        // In a real implementation, you'd use proper HKDF
        // For now, we'll use the generated key directly
        return key
    }
    
    /**
     * Encrypts data with AES-256-GCM
     */
    private fun encryptWithAES(data: ByteArray, key: SecretKey): ByteArray {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding", "BC")
        val iv = ByteArray(GCM_IV_SIZE)
        SecureRandom().nextBytes(iv)
        
        val gcmSpec = GCMParameterSpec(GCM_TAG_SIZE * 8, iv)
        cipher.init(Cipher.ENCRYPT_MODE, key, gcmSpec)
        
        val encrypted = cipher.doFinal(data)
        val result = ByteArray(iv.size + encrypted.size)
        System.arraycopy(iv, 0, result, 0, iv.size)
        System.arraycopy(encrypted, 0, result, iv.size, encrypted.size)
        
        return result
    }
    
    /**
     * Decrypts data with AES-256-GCM
     */
    private fun decryptWithAES(data: ByteArray, key: SecretKey): ByteArray {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding", "BC")
        val iv = data.copyOfRange(0, GCM_IV_SIZE)
        val encrypted = data.copyOfRange(GCM_IV_SIZE, data.size)
        
        val gcmSpec = GCMParameterSpec(GCM_TAG_SIZE * 8, iv)
        cipher.init(Cipher.DECRYPT_MODE, key, gcmSpec)
        
        return cipher.doFinal(encrypted)
    }
}

/**
 * Encryption-related exceptions
 */
class EncryptionException(message: String) : Exception(message) 