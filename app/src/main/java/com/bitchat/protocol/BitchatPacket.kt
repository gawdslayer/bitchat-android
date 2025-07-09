package com.bitchat.protocol

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Represents a Bitchat message packet in the mesh network.
 * This matches the iOS implementation structure exactly.
 */
@Parcelize
data class BitchatPacket(
    val version: UByte = 1u,
    val type: UByte,
    val ttl: UByte,
    val timestamp: ULong,
    val senderID: ByteArray,
    val recipientID: ByteArray? = null,
    val payload: ByteArray,
    val signature: ByteArray? = null
) : Parcelable {
    
    companion object {
        // Packet types matching iOS implementation
        const val TYPE_MESSAGE = 1u
        const val TYPE_KEY_EXCHANGE = 2u
        const val TYPE_PEER_ANNOUNCEMENT = 3u
        const val TYPE_CHANNEL_JOIN = 4u
        const val TYPE_CHANNEL_LEAVE = 5u
        const val TYPE_PRIVATE_MESSAGE = 6u
        const val TYPE_ACK = 7u
        const val TYPE_FRAGMENT = 8u
        
        // Protocol constants
        const val HEADER_SIZE = 13
        const val SENDER_ID_SIZE = 8
        const val RECIPIENT_ID_SIZE = 8
        const val SIGNATURE_SIZE = 64
        const val MAX_TTL = 7u
        const val MAX_PAYLOAD_SIZE = 500
    }
    
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BitchatPacket

        if (version != other.version) return false
        if (type != other.type) return false
        if (ttl != other.ttl) return false
        if (timestamp != other.timestamp) return false
        if (!senderID.contentEquals(other.senderID)) return false
        if (recipientID != null && other.recipientID != null) {
            if (!recipientID.contentEquals(other.recipientID)) return false
        } else if (recipientID != other.recipientID) return false
        if (!payload.contentEquals(other.payload)) return false
        if (signature != null && other.signature != null) {
            if (!signature.contentEquals(other.signature)) return false
        } else if (signature != other.signature) return false

        return true
    }

    override fun hashCode(): Int {
        var result = version.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + ttl.hashCode()
        result = 31 * result + timestamp.hashCode()
        result = 31 * result + senderID.contentHashCode()
        result = 31 * result + (recipientID?.contentHashCode() ?: 0)
        result = 31 * result + payload.contentHashCode()
        result = 31 * result + (signature?.contentHashCode() ?: 0)
        return result
    }
    
    /**
     * Creates a new packet with decremented TTL for relaying
     */
    fun decrementTTL(): BitchatPacket? {
        return if (ttl > 0u) {
            copy(ttl = ttl - 1u)
        } else {
            null // TTL expired
        }
    }
    
    /**
     * Checks if this packet is for a specific recipient
     */
    fun isForRecipient(recipientID: ByteArray): Boolean {
        return this.recipientID?.contentEquals(recipientID) == true
    }
    
    /**
     * Checks if this packet is a broadcast message
     */
    fun isBroadcast(): Boolean {
        return recipientID == null
    }
    
    /**
     * Gets a unique identifier for this packet
     */
    fun getMessageId(): String {
        return "${senderID.toHexString()}_${timestamp}_${type}"
    }
    
    /**
     * Converts byte array to hex string for debugging
     */
    private fun ByteArray.toHexString(): String {
        return joinToString("") { "%02x".format(it) }
    }
} 