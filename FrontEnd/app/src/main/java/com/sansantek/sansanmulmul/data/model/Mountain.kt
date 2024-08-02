package com.sansantek.sansanmulmul.data.model


//data class Mountain(val mountainImg: Int, val mountainName: String, val courseCnt: Int)
// json으로 응답 오는 걸 보면 mountain이라는 key 아래 아래의 key value 쌍의 객체가 주어진다, key에 맞춰서 dataclass의 property를 동일하게 설정하기
data class Mountain(
    val mountainId: Int,
    val mountainCode: Int,
    val mountainName: String,
    val mountainLocation: String,
    val mountainHeight: Int,
    val mountainDescription: String?,
    val mountainImg: String?,
    val mountainWeather: String,
    val mountainLat: Double,
    val mountainLon: Double,
    val mountainSpots: List<MountainSpot>
)
