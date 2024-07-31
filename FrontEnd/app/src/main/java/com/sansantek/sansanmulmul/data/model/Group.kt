package com.sansantek.sansanmulmul.data.model

data class Group(val title: String, var imageByte: ByteArray? = null) {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Group

    if (title != other.title) return false
    if (!imageByte.contentEquals(other.imageByte)) return false

    return true
  }

  override fun hashCode(): Int {
    var result = title.hashCode()
    result = 31 * result + imageByte.contentHashCode()
    return result
  }
}
