package com.sansantek.sansanmulmul.ui.util

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.util.TypedValue
import com.sansantek.sansanmulmul.R
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.text.NumberFormat
import java.util.Locale

private const val TAG = "Util 싸피"

object Util {
  val Float.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

  fun Float.dpToPx(context: Context): Float {
    return TypedValue.applyDimension(
      TypedValue.COMPLEX_UNIT_DIP,
      this,
      context.resources.displayMetrics
    )
  }

  fun byteArrayToBitmap(byteArray: ByteArray): Bitmap {
    return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
  }

  fun pngToByteArray(context: Context, img: Int): ByteArray {
    // drawable 리소스를 InputStream으로 가져오기
    val inputStream: InputStream = context.resources.openRawResource(img)

    // InputStream을 ByteArray로 변환
    val byteArray = inputStreamToByteArray(inputStream)

    return byteArray
  }

  private fun inputStreamToByteArray(inputStream: InputStream): ByteArray {
    val byteBuffer = ByteArrayOutputStream()
    val bufferSize = 1024
    val buffer = ByteArray(bufferSize)

    var len = 0
    while (inputStream.read(buffer).also { len = it } != -1) {
      byteBuffer.write(buffer, 0, len)
    }

    return byteBuffer.toByteArray()
  }

  // ~님이 ~요청했습니다! 까지 초록색으로 바꾸는 코드
  fun extractJoinRequests(context: Context, text: String): SpannableString? {
    val regex = "(.*?) 님이 그룹 가입을 요청했습니다!".toRegex()
    val spannableString = SpannableString(text)
    var check = false
    regex.findAll(text).forEach { matchResult ->
      check = true
      val start = matchResult.range.first
      val end = matchResult.range.last + 1
      spannableString.setSpan(
        ForegroundColorSpan(context.getColor(R.color.group_detail_alarm_green_btn)),
        start,
        end,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
      )
    }
    return if (check) spannableString else {
      null
    }
  }

  // 등산 일정이 ~ 로 변경되었습니다에서 ~에 해당하는 부분을 초록색으로 바꾸는 코드
  fun extractHikingSchedule(context: Context, text: String): SpannableString? {
    val regex = "등산 일정이 (.*?)로 변경되었습니다".toRegex()
    val spannableString = SpannableString(text)
    var check = false
    regex.findAll(text).forEach { matchResult ->
      check = true
      val start = matchResult.range.first
      val end = matchResult.range.last
      val courseStart = matchResult.groups[1]?.range?.first ?: start
      val courseEnd = matchResult.groups[1]?.range?.last?.plus(1) ?: end
      Log.d(TAG, "extractHikingCourse: $courseStart, $courseEnd")
      spannableString.setSpan(
        ForegroundColorSpan(context.getColor(R.color.group_detail_alarm_green_btn)),
        courseStart,
        courseEnd,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
      )
    }
    return if (check) spannableString else {
      null
    }
  }

  // 등산 코스가 ~ 로 변경되었습니다에서 ~에 해당하는 부분을 초록색으로 바꾸는 코드
  fun extractHikingCourse(context: Context, text: String): SpannableString? {
    Log.d(TAG, "extractHikingCourse: ")
    val regex = "등산 코스가 (.*?)로 변경되었습니다".toRegex()
    val spannableString = SpannableString(text)
    var check = false
    regex.findAll(text).forEach { matchResult ->
      check = true
      val start = matchResult.range.first
      val end = matchResult.range.last
      val courseStart = matchResult.groups[1]?.range?.first ?: start
      val courseEnd = matchResult.groups[1]?.range?.last?.plus(1) ?: end
      Log.d(TAG, "extractHikingCourse: $courseStart, $courseEnd")
      spannableString.setSpan(
        ForegroundColorSpan(context.getColor(R.color.group_detail_alarm_green_btn)),
        courseStart,
        courseEnd,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
      )
    }
    Log.d(TAG, "extractHikingSchedule: 등상 코스 변경으로 감지")
    return if (check) spannableString else {
      null
    }
  }

  // 시간을 0000 형식 -> 00:00 형식으로 바꿔주는 메소드
  fun formatSunRiseSunSetTime(time: String): String {
    if (time.length != 4) {
      throw IllegalArgumentException("Input must be a 4-digit string")
    }

    val hour = time.substring(0, 2)
    val minute = time.substring(2, 4)

    return "$hour:$minute"
  }

  // 숫자 단위에 맞게 세자리마다 콤마를 찍어주는 메소드
  fun getNumberWithCommas(number: Int): String {
    val numberFormat = NumberFormat.getNumberInstance(Locale.US)
    return numberFormat.format(number)
  }

  fun makeHeaderByAccessToken(accessToken: String): String{
    return "Bearer $accessToken"
  }

}