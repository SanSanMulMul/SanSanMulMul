package com.sansantek.sansanmulmul.data.model

data class Recommendation(
  val mountainName: String,
  val mountainHeight: Int,
  val mountainImg: String?,
) {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Recommendation

    if (mountainName != other.mountainName) return false
    if (mountainHeight != other.mountainHeight) return false
    if (mountainImg != other.mountainImg) return false

    return true
  }

  override fun hashCode(): Int {
    var result = mountainName.hashCode()
    result = 31 * result + mountainHeight.hashCode()
    result = 31 * result + mountainImg.hashCode()
    return result
  }
}
