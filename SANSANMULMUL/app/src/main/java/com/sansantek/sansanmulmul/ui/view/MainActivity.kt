package com.sansantek.sansanmulmul.ui.view

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseActivity
import com.sansantek.sansanmulmul.databinding.ActivityMainBinding
import com.sansantek.sansanmulmul.ui.view.register.RegisterStartFragment

private const val TAG = "MainActivity 싸피"
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: ")

        setContentView(binding.root)

        supportFragmentManager.beginTransaction().replace(binding.fragmentView.id, RegisterStartFragment()).commit()
    }
}