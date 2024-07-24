package com.sansantek.sansanmulmul.data.model

data class Recommendation(
  val mountainName: String,
  val mountainDifficulty: String,
  val mountainImg: Int
) {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Recommendation

    if (mountainName != other.mountainName) return false
    if (mountainDifficulty != other.mountainDifficulty) return false
    if (mountainImg != other.mountainImg) return false

    return true
  }

  override fun hashCode(): Int {
    var result = mountainName.hashCode()
    result = 31 * result + mountainDifficulty.hashCode()
    result = 31 * result + mountainImg.hashCode()
    return result
  }
}
