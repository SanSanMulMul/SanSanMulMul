package com.sansantek.sansanmulmul.ui.view


import android.Manifest
import android.annotation.SuppressLint
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
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.ApplicationClass.Companion.sharedPreferencesUtil
import com.sansantek.sansanmulmul.config.BaseActivity
import com.sansantek.sansanmulmul.config.Const.Companion.REQUEST_IMAGE_CAPTURE
import com.sansantek.sansanmulmul.databinding.ActivityMainBinding
import com.sansantek.sansanmulmul.ui.view.grouptab.GroupTabFragment
import com.sansantek.sansanmulmul.ui.view.hikingrecordingtab.HikingRecordingTabFragment
import com.sansantek.sansanmulmul.ui.view.hometab.HomeTabFragment
import com.sansantek.sansanmulmul.ui.view.maptab.MapTabFragment
import com.sansantek.sansanmulmul.ui.view.mypagetab.MyPageTabFragment
import com.sansantek.sansanmulmul.ui.util.PermissionChecker
import com.sansantek.sansanmulmul.ui.util.RetrofiltUtil.Companion.userService
import com.sansantek.sansanmulmul.ui.util.Util.makeHeaderByAccessToken
import com.sansantek.sansanmulmul.ui.viewmodel.MainActivityViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

private const val TAG = "MainActivity 싸피"

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    private var photoURI: Uri? = null
    private lateinit var currentPhotoPath: String
    private var bitmap: Bitmap? = null
    private val activityViewModel : MainActivityViewModel by viewModels()

    /** permission check **/
    private val checker = PermissionChecker(this)
    private val runtimePermissions =
        arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)

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
        loadUserProfile()
        loadUserHikingStyle()

        initBottomNav()
        changeFragment(HomeTabFragment())
    }

    private fun loadUserProfile(){
        lifecycleScope.launch(Dispatchers.IO) {
            activityViewModel.token?.let {
                val user = userService.loadUserProfile(makeHeaderByAccessToken(it.accessToken))
                if (user.code() == 200) {
                    user.body()?.let { usr ->
                        activityViewModel.setUser(usr)
                    }
                }
            }
        }
    }

    private fun loadUserHikingStyle(){
        activityViewModel.token?.let {
            lifecycleScope.launch(Dispatchers.Main) {
                activityViewModel.setHikingStyles(userService.getHikingStyle(makeHeaderByAccessToken(it.accessToken)))
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
        val fragment =
            supportFragmentManager.findFragmentById(R.id.fragment_view) as HikingRecordingTabFragment
        Log.d(TAG, "onActivityResult: $bitmap")
//        fragment.setImageBitmap(bitmap)
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