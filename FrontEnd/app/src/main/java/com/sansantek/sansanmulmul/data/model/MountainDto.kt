package com.sansantek.sansanmulmul.data.model


//data class Mountain(val mountainImg: Int, val mountainName: String, val courseCnt: Int)
// json으로 응답 오는 걸 보면 mountain이라는 key 아래 아래의 key value 쌍의 객체가 주어진다, key에 맞춰서 dataclass의 property를 동일하게 설정하기
data class MountainDto(val mountain_id: Int, val mountain_code: Int, val mountainName: String, val mountain_location: String, val mountain_height: Int, val mountain_description: String, val mountain_img: Int?, val mountain_weather: String, val mountain_lat: Double, val mountain_lon: Double, val mountain_spots: List<MountainSpot>)