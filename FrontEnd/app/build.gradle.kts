plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.jetbrains.kotlin.android)
}

android {
  namespace = "com.sansantek.sansanmulmul"
  compileSdk = 34

  defaultConfig {
    applicationId = "com.sansantek.sansanmulmul"
    minSdk = 24
    targetSdk = 34
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

  implementation(libs.google.flexbox)

  implementation(libs.android.segmented)


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
  // androidx.appcompat:appcompat:1.6.1
  implementation(libs.androidx.appcompat.v161)

  // com.google.android.material:material:1.13.0-alpha04
  implementation(libs.material.v1130alpha04)

  // gson
  implementation(libs.gson)

  implementation(libs.androidx.constraintlayout)

  implementation(libs.androidx.core.ktx)

  // RecyclerView
  implementation(libs.androidx.recyclerview)


  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.appcompat)
  implementation(libs.material)
  implementation(libs.androidx.activity)
  implementation(libs.androidx.constraintlayout)
  implementation(libs.androidx.viewbinding)
  implementation("androidx.appcompat:appcompat:1.6.1")
  implementation("com.google.android.material:material:1.13.0-alpha04")
  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso.core)
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.appcompat)
  implementation(libs.material)
  implementation(libs.androidx.activity)
  implementation(libs.androidx.constraintlayout)
  implementation(libs.androidx.viewbinding)

  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso.core)
}