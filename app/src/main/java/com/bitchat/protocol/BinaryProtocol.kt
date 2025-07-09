package com.bitchat.protocol

import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 * Binary protocol implementation matching the iOS version exactly.
 * Handles encoding and decoding of BitchatPacket objects to/from binary format.
 */
object BinaryProtocol {
    
    // Protocol constants matching iOS implementation
    const val HEADER_SIZE = 13
    const val SENDER_ID_SIZE = 8
    const val RECIPIENT_ID_SIZE = 8
    const val SIGNATURE_SIZE = 64
    
    object Flags {
        const val HAS_RECIPIENT: UByte = 0x01u
        const val HAS_SIGNATURE: UByte = 0x02u
        const val IS_COMPRESSED: UByte = 0x04u
    }
    
    /**
     * Encodes a BitchatPacket to binary format
     */
    fun encode(packet: BitchatPacket): ByteArray? {
        return try {
            val buffer = ByteBuffer.allocate(calculatePacketSize(packet))
            buffer.order(ByteOrder.BIG_ENDIAN)
            
            // Header (13 bytes)
            buffer.put(packet.version.toByte())
            buffer.put(packet.type.toByte())
            buffer.put(packet.ttl.toByte())
            
            // Timestamp (8 bytes, big-endian)
            buffer.putLong(packet.timestamp.toLong())
            
            // Flags
            var flags: UByte = 0u
            if (packet.recipientID != null) flags = flags or Flags.HAS_RECIPIENT
            if (packet.signature != null) flags = flags or Flags.HAS_SIGNATURE
            // Note: Compression flag would be set here if compression is implemented
            buffer.put(flags.toByte())
            
            // Payload length (2 bytes, big-endian)
            val payloadLength = packet.payload.size.toUShort()
            buffer.putShort(payloadLength.toShort())
            
            // SenderID (exactly 8 bytes)
            val senderBytes = packet.senderID.copyOf(SENDER_ID_SIZE)
            buffer.put(senderBytes)
            
            // RecipientID (if present)
            if (packet.recipientID != null) {
                val recipientBytes = packet.recipientID.copyOf(RECIPIENT_ID_SIZE)
                buffer.put(recipientBytes)
            }
            
            // Payload
            buffer.put(packet.payload)
            
            // Signature (if present)
            if (packet.signature != null) {
                val signatureBytes = packet.signature.copyOf(SIGNATURE_SIZE)
                buffer.put(signatureBytes)
            }
            
            buffer.array()
        } catch (e: Exception) {
            null
        }
    }
    
    /**
     * Decodes binary data to a BitchatPacket
     */
    fun decode(data: ByteArray): BitchatPacket? {
        return try {
            if (data.size < HEADER_SIZE + SENDER_ID_SIZE) return null
            
            val buffer = ByteBuffer.wrap(data)
            buffer.order(ByteOrder.BIG_ENDIAN)
            
            // Header
            val version = buffer.get().toUByte()
            if (version != 1u) return null // Only support version 1
            
            val type = buffer.get().toUByte()
            val ttl = buffer.get().toUByte()
            val timestamp = buffer.getLong().toULong()
            
            // Flags
            val flags = buffer.get().toUByte()
            val hasRecipient = (flags and Flags.HAS_RECIPIENT) != 0u
            val hasSignature = (flags and Flags.HAS_SIGNATURE) != 0u
            val isCompressed = (flags and Flags.IS_COMPRESSED) != 0u
            
            // Payload length
            val payloadLength = buffer.getShort().toUShort()
            
            // Calculate expected total size
            var expectedSize = HEADER_SIZE + SENDER_ID_SIZE + payloadLength.toInt()
            if (hasRecipient) expectedSize += RECIPIENT_ID_SIZE
            if (hasSignature) expectedSize += SIGNATURE_SIZE
            
            if (data.size < expectedSize) return null
            
            // SenderID
            val senderID = ByteArray(SENDER_ID_SIZE)
            buffer.get(senderID)
            
            // RecipientID
            val recipientID = if (hasRecipient) {
                val recipient = ByteArray(RECIPIENT_ID_SIZE)
                buffer.get(recipient)
                recipient
            } else null
            
            // Payload
            val payload = ByteArray(payloadLength.toInt())
            buffer.get(payload)
            
            // Handle compression if needed
            val finalPayload = if (isCompressed) {
                // TODO: Implement LZ4 decompression
                payload
            } else {
                payload
            }
            
            // Signature
            val signature = if (hasSignature) {
                val sig = ByteArray(SIGNATURE_SIZE)
                buffer.get(sig)
                sig
            } else null
            
            BitchatPacket(
                version = version,
                type = type,
                ttl = ttl,
                timestamp = timestamp,
                senderID = senderID,
                recipientID = recipientID,
                payload = finalPayload,
                signature = signature
            )
        } catch (e: Exception) {
            null
        }
    }
    
    /**
     * Calculates the total size of a packet when encoded
     */
    private fun calculatePacketSize(packet: BitchatPacket): Int {
        var size = HEADER_SIZE + SENDER_ID_SIZE + packet.payload.size
        if (packet.recipientID != null) size += RECIPIENT_ID_SIZE
        if (packet.signature != null) size += SIGNATURE_SIZE
        return size
    }
    
    /**
     * Utility function to convert byte array to hex string
     */
    fun ByteArray.toHexString(): String {
        return joinToString("") { "%02x".format(it) }
    }
    
    /**
     * Utility function to convert hex string to byte array
     */
    fun String.fromHexString(): ByteArray {
        return chunked(2).map { it.toInt(16).toByte() }.toByteArray()
    }
} 