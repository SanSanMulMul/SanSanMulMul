package com.sansantek.sansanmulmul.config

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.threetenabp.AndroidThreeTen
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import com.sansantek.sansanmulmul.BuildConfig
import com.sansantek.sansanmulmul.data.local.SharedPreferencesUtil
import com.sansantek.sansanmulmul.data.repository.LocationHistoryRepository
import com.sansantek.sansanmulmul.data.repository.StepCounterRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

private const val TAG = "ApplicationClass_싸피"

class ApplicationClass : Application() {
  companion object {
    const val SERVER_URL = BuildConfig.SERVER_IP
    lateinit var sharedPreferencesUtil: SharedPreferencesUtil
    lateinit var retrofit: Retrofit
  }


    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate: ${Utility.getKeyHash(this)}")
        KakaoSdk.init(this, BuildConfig.NATIVE_APP_KEY)
        AndroidThreeTen.init(this)
        FirebaseApp.initializeApp(this)
        //shared preference 초기화
        sharedPreferencesUtil = SharedPreferencesUtil(applicationContext)
        StepCounterRepository.initialize(this)
        LocationHistoryRepository.initialize(this)
//        ViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(this)
//            .create(MainActivityViewModel::class.java)

    // 레트로핏 인스턴스를 생성하고, 레트로핏에 각종 설정값들을 지정해줍니다.
    // 연결 타임아웃시간은 5초로 지정이 되어있고, HttpLoggingInterceptor를 붙여서 어떤 요청이 나가고 들어오는지를 보여줍니다.
    val okHttpClient = OkHttpClient.Builder()
      .readTimeout(5000, TimeUnit.MILLISECONDS)
      .connectTimeout(5000, TimeUnit.MILLISECONDS)
      .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
      .build()

    // 앱이 처음 생성되는 순간, retrofit 인스턴스를 생성
    retrofit = Retrofit.Builder()
      .baseUrl(SERVER_URL)
      .addConverterFactory(ScalarsConverterFactory.create())
      .addConverterFactory(GsonConverterFactory.create(gson))
      .client(okHttpClient)
      .build()
  }

  //GSon은 엄격한 json type을 요구하는데, 느슨하게 하기 위한 설정. success, fail이 json이 아니라 단순 문자열로 리턴될 경우 처리..
  val gson: Gson = GsonBuilder()
    .setLenient()
    .create()
}