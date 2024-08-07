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
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseActivity
import com.sansantek.sansanmulmul.config.Const.Companion.REQUEST_IMAGE_CAPTURE
import com.sansantek.sansanmulmul.databinding.ActivityMainBinding
import com.sansantek.sansanmulmul.ml.Model
import com.sansantek.sansanmulmul.ui.util.PermissionChecker
import com.sansantek.sansanmulmul.ui.view.grouptab.GroupTabFragment
import com.sansantek.sansanmulmul.ui.view.hikingrecordingtab.HikingRecordingTabFragment
import com.sansantek.sansanmulmul.ui.view.hometab.HomeTabFragment
import com.sansantek.sansanmulmul.ui.view.maptab.MapTabFragment
import com.sansantek.sansanmulmul.ui.view.mypagetab.MyPageTabFragment
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
    private val imageSize = 224

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

        initBottomNav()
        changeFragment(HomeTabFragment())
    }


    @SuppressLint("QueryPermissionsNeeded")
    private fun openCamera() {
        Log.d(TAG, "openCamera: ")
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
        val softwareBitmap = bitmap?.copy(Bitmap.Config.ARGB_8888, true)
        val image =
            softwareBitmap?.let { Bitmap.createScaledBitmap(it, imageSize, imageSize, false) }
        if (image != null) {
            Log.d(TAG, "onActivityResult: image null 아님!")
            classifyImage(image)
        }
//        fragment.setImageBitmap(bitmap)
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

            // TODO : result인 산에 누끼딴 사진 입히기~!

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