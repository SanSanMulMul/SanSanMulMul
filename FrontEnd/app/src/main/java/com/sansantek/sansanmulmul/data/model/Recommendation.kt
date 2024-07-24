package com.sansantek.sansanmulmul.data.model

data class Recommendation(val mountainName: String, val difficulty: String) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Group

        if (mountainName != other.title) return false
        if (difficulty != other.title) return false

        return true
    }

    override fun hashCode(): Int {
        var result = mountainName.hashCode()
        result = 31 * result + difficulty.hashCode()
        return result
    }
}
