package com.sansantek.sansanmulmul.ui.view


import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseActivity
import com.sansantek.sansanmulmul.config.Const.Companion.REQUEST_IMAGE_CAPTURE
import com.sansantek.sansanmulmul.databinding.ActivityMainBinding
import com.sansantek.sansanmulmul.ml.Model
import com.sansantek.sansanmulmul.ui.util.PermissionChecker
import com.sansantek.sansanmulmul.ui.util.RetrofiltUtil.Companion.fcmService
import com.sansantek.sansanmulmul.ui.util.RetrofiltUtil.Companion.mountainService
import com.sansantek.sansanmulmul.ui.util.RetrofiltUtil.Companion.userService
import com.sansantek.sansanmulmul.ui.util.Util.makeHeaderByAccessToken
import com.sansantek.sansanmulmul.ui.view.grouptab.GroupTabFragment
import com.sansantek.sansanmulmul.ui.view.hikingrecordingtab.HikingRecordingTabFragment
import com.sansantek.sansanmulmul.ui.view.hikingrecordingtab.StoneAddSuccessDialog
import com.sansantek.sansanmulmul.ui.view.hometab.HomeTabFragment
import com.sansantek.sansanmulmul.ui.view.maptab.MapTabFragment
import com.sansantek.sansanmulmul.ui.view.mypagetab.MyPageTabFragment
import com.sansantek.sansanmulmul.ui.viewmodel.HikingRecordingTabViewModel
import com.sansantek.sansanmulmul.ui.viewmodel.MainActivityViewModel
import com.sansantek.sansanmulmul.ui.viewmodel.MountainPeakStoneViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.File
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.text.SimpleDateFormat
import java.util.Date

private const val TAG = "MainActivity 싸피"

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

  private var photoURI: Uri? = null
  private lateinit var currentPhotoPath: String
  private var bitmap: Bitmap? = null
  private val activityViewModel: MainActivityViewModel by viewModels()
  private val hikingRecordingTabViewModel: HikingRecordingTabViewModel by viewModels()
  private val mountainPeakStoneViewModel: MountainPeakStoneViewModel by viewModels()
  private val imageSize = 224


  /** permission check **/
  private val checker = PermissionChecker(this)
  private val runtimePermissions = if (Build.VERSION.SDK_INT < 29) {
    arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
  } else {
    arrayOf(Manifest.permission.CAMERA)
  }

  fun checkPermission() {
    if (!checker.checkPermission(this, runtimePermissions)) {
      checker.setOnGrantedListener { //퍼미션 획득 성공일때
        openCamera()
      }
      checker.requestPermissionLauncher.launch(runtimePermissions) // 권한없으면 창 띄움
    } else { //이미 전체 권한이 있는 경우
      openCamera()
    }
  }

  /** permission check **/

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(binding.root)
    lifecycleScope.launch(Dispatchers.IO) {
      launch(Dispatchers.IO) {
        loadUserProfile()
      }
      launch(Dispatchers.IO) {
        loadUserHikingStyle()
      }
      launch(Dispatchers.IO) {
        loadFollowInfo()
      }
      launch(Dispatchers.IO) {
        loadLikedMountainList()
      }
      launch(Dispatchers.IO) {
        loadMyPageInfo()
      }

      changeFragment(HomeTabFragment())
    }
    initFCM()
    initBottomNav()

    // QR코드 인식 후
    val intent = intent
    if (Intent.ACTION_VIEW == intent.action) {
      val uri = intent.data
      if (uri != null) {
        val fragmentName = uri.getQueryParameter("fragment")
        if (fragmentName != null) {
          hikingRecordingTabViewModel.setIsQRScanned(true)
          binding.mainLayoutBottomNavigation.selectedItemId =
            R.id.mountain // 바로 등산기록 탭으로 이동
        }
      }
    }
  }

  private fun initFCM() {
    // FCM 토큰 수신
    FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
      if (!task.isSuccessful) {
        Log.d(TAG, "FCM 토큰 얻기에 실패하였습니다.", task.exception)
        return@OnCompleteListener
      }
      // token log 남기기
      Log.d(TAG, "FCMtoken: ${task.result ?: "task.result is null"}")
      uploadToken(task.result)

    })
  }

  private fun uploadToken(token: String) {
    // 새로운 토큰 수신 시 서버로 전송
    activityViewModel.token?.let {
      lifecycleScope.launch {
        val result = fcmService.initFcmToken(makeHeaderByAccessToken(it.accessToken), token)
        if (result.isSuccessful) {
          val res = result.body()
          createNotificationChannel("noti", "noti")
          Log.d(TAG, "FCMonResponse: $res")
        } else {
          Log.d(TAG, "FCMonResponse: Error Code ${result.code()}")
        }
      }
    }
  }

  // NotificationChannel 설정
  private fun createNotificationChannel(id: String, name: String) {
    val importance = NotificationManager.IMPORTANCE_DEFAULT
    val channel = NotificationChannel(id, name, importance)

    val notificationManager: NotificationManager =
      getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(channel)
  }

  private fun loadLikedMountainList() {
    lifecycleScope.launch(Dispatchers.Main) {
      activityViewModel.token?.let {
        launch(Dispatchers.Main) {
          val likedMountains =
            mountainService.getLikedMountainList(makeHeaderByAccessToken(it.accessToken))
          if (likedMountains.code() == 200) {
            launch(Dispatchers.Main) {
              likedMountains.body()?.let {
                activityViewModel.setLikedMountainList(it)
              }
            }
          }
        }
      }
    }
  }

  private fun loadMyPageInfo() {
    lifecycleScope.launch(Dispatchers.Main) {
      activityViewModel.token?.let {
        launch(Dispatchers.Main) {
          val myPageInfo =
            userService.getMyPageInfo(makeHeaderByAccessToken(it.accessToken))
          activityViewModel.setMyPageInfo(myPageInfo)
        }
      }
    }
  }

  private fun loadFollowInfo() {
    lifecycleScope.launch(Dispatchers.Main) {
      activityViewModel.token?.let {
        launch(Dispatchers.Main) {
          val followerResult =
            userService.getUserFollower(makeHeaderByAccessToken(it.accessToken))
          activityViewModel.setFollowerList(followerResult)
        }
        launch(Dispatchers.Main) {
          val followingResult =
            userService.getUserFollowing(makeHeaderByAccessToken(it.accessToken))
          activityViewModel.setFollowingList(followingResult)
        }
      }
    }
  }

  private fun loadUserProfile() {
    lifecycleScope.launch(Dispatchers.Main) {
      activityViewModel.token?.let {
        val user = userService.loadUserProfile(makeHeaderByAccessToken(it.accessToken))
        if (user.code() == 200) {
          user.body()?.let { usr ->
            activityViewModel.setUser(usr)
            activityViewModel.setUserProfileImgUrl(usr.userProfileImg)
            activityViewModel.setUserTitle(usr.userStaticBadge)
          }
        }
      }
    }
  }

  private fun loadUserHikingStyle() {
    activityViewModel.token?.let {
      lifecycleScope.launch(Dispatchers.Main) {
        activityViewModel.setHikingStyles(
          userService.getHikingStyle(makeHeaderByAccessToken(it.accessToken)).sorted()
        )
      }
    }
  }

  @SuppressLint("QueryPermissionsNeeded")
  private fun openCamera() {
    Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
      takePictureIntent.resolveActivity(packageManager)?.also {
        val photoFile: File? = try {
          createImageFile()
        } catch (ex: IOException) {
          null
        }
        Log.d(TAG, "openCamera: ph")
        photoFile?.also {
          photoURI = FileProvider.getUriForFile(
            this,
            "com.sansantek.sansanmulmul.fileprovider",
            it
          )
          takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
          startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
      }
    }
  }


  @Throws(IOException::class)
  private fun createImageFile(): File {
    val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
    return File.createTempFile(
      "JPEG_${timeStamp}_",
      ".jpg",
      storageDir
    ).apply {
      currentPhotoPath = absolutePath
    }
  }


  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    try {
      Log.d(TAG, "onActivityResult: 여기를 왜가?")
      if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
        bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
          ImageDecoder.decodeBitmap(
            ImageDecoder.createSource(
              contentResolver,
              photoURI!!
            )
          )
        } else {
          MediaStore.Images.Media.getBitmap(contentResolver, photoURI)
        }
      }
    } catch (e: IOException) {
      null
    }
    Log.d(TAG, "onActivityResult: $bitmap")
    val softwareBitmap = bitmap?.copy(Bitmap.Config.ARGB_8888, true)
    val image =
      softwareBitmap?.let { Bitmap.createScaledBitmap(it, imageSize, imageSize, false) }
    if (image != null) {
      val dialog = bitmap?.let { StoneAddSuccessDialog(it) }
      dialog?.show(supportFragmentManager, "StoneAddSuccessDialog")
      classifyImage(image)
    }
  }

  private fun classifyImage(image: Bitmap) {
    try {
      val model = Model.newInstance(applicationContext)

      val inputFeature0 =
        TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
      val byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3)
      byteBuffer.order(ByteOrder.nativeOrder())

      val intValues = IntArray(imageSize * imageSize)
      image.getPixels(intValues, 0, image.width, 0, 0, image.width, image.height)

      var pixel = 0
      for (i in 0 until imageSize) {
        for (j in 0 until imageSize) {
          val `val` = intValues[pixel++] // RGB
          byteBuffer.putFloat(((`val` shr 16) and 0xFF) * (1f / 255f))
          byteBuffer.putFloat(((`val` shr 8) and 0xFF) * (1f / 255f))
          byteBuffer.putFloat((`val` and 0xFF) * (1f / 255f))
        }
      }

      inputFeature0.loadBuffer(byteBuffer)

      val outputs = model.process(inputFeature0)
      val outputFeature0 = outputs.outputFeature0AsTensorBuffer

      val confidences = outputFeature0.floatArray
      var maxPos = 0
      var maxConfidence = 0f
      for (i in confidences.indices) {
        if (confidences[i] > maxConfidence) {
          maxConfidence = confidences[i]
          maxPos = i
        }
      }
      val classes = arrayOf(
        "가리산", "가리왕산", "가야산", "가지산", "감악산", "강천산", "계룡산", "계방산",
        "공작산", "관악산", "구병산", "금산", "금수산", "금오산", "금정산", "깃대봉"
      )

      var s = ""
      for (i in classes.indices) {
        s += String.format("%s: %.1f%%\n", classes[i], confidences[i] * 100)
      }

      Log.d(TAG, "classifyImage: result = ${classes[maxPos]} / confidence = $s")

      val result = classes[maxPos]
//      mountainPeakStoneViewModel.stoneId =
      model.close()
    } catch (e: IOException) {
      // TODO Handle the exception
    }
  }


  private fun initBottomNav() {

    binding.mainLayoutBottomNavigation.setOnItemSelectedListener {
//            Log.d(TAG, "initBottomNav: ${it.itemId}")
      when (it.itemId) {
        R.id.home -> {
          changeFragment(HomeTabFragment())
        }

        R.id.map -> {
          changeFragment(MapTabFragment())
        }

        R.id.mountain -> {
          changeFragment(HikingRecordingTabFragment())
        }

        R.id.group -> {
          changeFragment(GroupTabFragment())
        }

        R.id.mypage -> {
          changeFragment(MyPageTabFragment())
        }

      }
      return@setOnItemSelectedListener true
    }
  }

  fun changeFragment(view: Fragment) {
    for (i in 0 until supportFragmentManager.backStackEntryCount) {
      supportFragmentManager.popBackStack()
    }
    supportFragmentManager.beginTransaction().replace(binding.fragmentView.id, view).commit()
  }

  fun changeAddToBackstackFragment(view: Fragment) {
    Log.d(TAG, "changeAddToBackstackFragment: fragment change")
    supportFragmentManager.beginTransaction().replace(binding.fragmentView.id, view)
      .addToBackStack(null).commit()
  }

  fun changeBottomNavigationVisibility(visible: Boolean) {
    if (visible) {
      binding.mainLayoutBottomNavigation.visibility = View.VISIBLE
    } else {
      binding.mainLayoutBottomNavigation.visibility = View.GONE
    }

  }


}