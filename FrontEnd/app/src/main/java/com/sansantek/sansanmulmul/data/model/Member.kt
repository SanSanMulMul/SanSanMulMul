package com.sansantek.sansanmulmul.data.model

data class Member(val title:String, var name: String, var registration: Boolean, var imageByte: ByteArray? = null) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Member

        if (title != other.title) return false
        if (name != other.name) return false
        if (registration != other.registration) return false
        if (imageByte != null) {
            if (other.imageByte == null) return false
            if (!imageByte.contentEquals(other.imageByte)) return false
        } else if (other.imageByte != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + registration.hashCode()
        result = 31 * result + (imageByte?.contentHashCode() ?: 0)
        return result
    }
}
