import java.io.FileInputStream
import java.util.Properties

plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.jetbrains.kotlin.android)
}
// local에서 키를 가져옴
val properties = Properties()
properties.load(FileInputStream(rootProject.file("local.properties")))
// local.properties에 naver_client_id="키값"으로 저장하면 됨
val naverClientId: String = properties.getProperty("naver_client_id")
// 카카오 앱 키 로컬에서 불러오기
val NATIVE_APP_KEY: String = properties.getProperty("NATIVE_APP_KEY")

android {
  namespace = "com.sansantek.sansanmulmul"
  compileSdk = 34

  defaultConfig {
    applicationId = "com.sansantek.sansanmulmul"
    minSdk = 24
    targetSdk = 34
    versionCode = 1
    versionName = "1.0"

    //    AndroidManifest에서 쓰기 위한 placeHolder
    manifestPlaceholders["naverClientId"]= naverClientId

    manifestPlaceholders["NATIVE_APP_KEY"]= NATIVE_APP_KEY

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  buildFeatures {
    buildConfig = true
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(
        getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro"
      )
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
  kotlinOptions {
    jvmTarget = "1.8"
  }

  dataBinding {
    enable = true
  }
  viewBinding {
    enable = true
  }
}

dependencies {

  //  카카오 관련 전체 모듈 설치
  // com.kakao.sdk:v2-all:2.20.3
  implementation(libs.v2.all)


  implementation (libs.glide)

  implementation(libs.google.flexbox)

  implementation(libs.android.segmented)
  implementation(fileTree(mapOf("dir" to "libs/android-segmented-control-master", "include" to listOf("*.aar", "*.jar"))))


  implementation(libs.dotsindicator)

  //framework ktx dependency 추가
  implementation(libs.androidx.fragment.ktx)

  // https://github.com/square/retrofit
  implementation(libs.retrofit)
  implementation(libs.converter.scalars)

  // https://github.com/square/okhttp
  implementation(libs.okhttp)

  // https://github.com/square/retrofit/tree/master/retrofit-converters/gson
  implementation(libs.converter.gson)
  implementation(libs.converter.scalars)


  // https://github.com/square/okhttp/tree/master/okhttp-logging-interceptor
  implementation(libs.logging.interceptor)

  // gson
  implementation(libs.gson)


  implementation(libs.androidx.constraintlayout)

  implementation(libs.androidx.core.ktx)

  // RecyclerView
  implementation(libs.androidx.recyclerview)

  // 네이버 지도 SDK
  implementation(libs.map.sdk)

  implementation(libs.androidx.activity)
  implementation(libs.androidx.constraintlayout)
  implementation(libs.androidx.viewbinding)
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.appcompat)
  implementation(libs.material)
  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso.core)

}