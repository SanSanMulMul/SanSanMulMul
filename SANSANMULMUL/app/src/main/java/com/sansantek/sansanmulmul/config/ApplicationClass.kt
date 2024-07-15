package com.sansantek.sansanmulmul.config

class ApplicationClass : Application() {
    lateinit var shoppingViewModel : MainActivityViewModel
    companion object {
        // ipconfig를 통해 ip확인하기
        // 핸드폰으로 접속은 같은 인터넷으로 연결 되어있어야함 (유,무선)

        // 싸피에서 쓰는 컴퓨터 주소
        const val SERVER_URL = "http://192.168.33.111:9987/"
        //const val SERVER_URL = "http://118.45.102.94:9987/"

        const val MENU_IMGS_URL = "${SERVER_URL}imgs/menu/"
        const val IMGS_URL = "${SERVER_URL}imgs/grade/"

        lateinit var sharedPreferencesUtil: SharedPreferencesUtil
        lateinit var retrofit: Retrofit

        // 주문 준비 완료 확인 시간 1분
        const val ORDER_COMPLETED_TIME = 60 * 1000

        var notiList = MutableLiveData<ArrayList<Notification>>(arrayListOf<Notification>())

        var USER_ID = ""
    }


    override fun onCreate() {
        super.onCreate()

        //shared preference 초기화
        sharedPreferencesUtil = SharedPreferencesUtil(applicationContext)

        shoppingViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(this)
            .create(MainActivityViewModel::class.java)

        // 레트로핏 인스턴스를 생성하고, 레트로핏에 각종 설정값들을 지정해줍니다.
        // 연결 타임아웃시간은 5초로 지정이 되어있고, HttpLoggingInterceptor를 붙여서 어떤 요청이 나가고 들어오는지를 보여줍니다.
        val okHttpClient = OkHttpClient.Builder()
            .readTimeout(5000, TimeUnit.MILLISECONDS)
            .connectTimeout(5000, TimeUnit.MILLISECONDS)
            // 로그캣에 okhttp.OkHttpClient로 검색하면 http 통신 내용을 보여줍니다.
//            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(AddCookiesInterceptor())
            .addInterceptor(ReceivedCookiesInterceptor()).build()

        // 앱이 처음 생성되는 순간, retrofit 인스턴스를 생성
        retrofit = Retrofit.Builder()
            .baseUrl(SERVER_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
        KakaoSdk.init(this, "ad465c3a23d4387e59fba24ac043eaaa")
    }

    //GSon은 엄격한 json type을 요구하는데, 느슨하게 하기 위한 설정. success, fail이 json이 아니라 단순 문자열로 리턴될 경우 처리..
    val gson: Gson = GsonBuilder()
        .setLenient()
        .create()
}