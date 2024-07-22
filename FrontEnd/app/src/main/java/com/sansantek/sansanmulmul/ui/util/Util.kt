package com.sansantek.sansanmulmul.ui.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.sansantek.sansanmulmul.R
import java.io.ByteArrayOutputStream
import java.io.InputStream


object Util {
    fun byteArrayToBitmap(byteArray: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

    fun pngToByteArray(context: Context, img: Int): ByteArray{
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
}