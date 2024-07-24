package com.sansantek.sansanmulmul.ui.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseActivity
import com.sansantek.sansanmulmul.databinding.ActivityMainBinding
import com.sansantek.sansanmulmul.ui.view.grouptab.GroupTabFragment
import com.sansantek.sansanmulmul.ui.view.register.RegisterFinishFragment
import com.sansantek.sansanmulmul.ui.view.register.RegisterStartFragment

private const val TAG = "MainActivity 싸피"
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: ")

        setContentView(binding.root)
        initBottomNav()



        supportFragmentManager.beginTransaction().replace(binding.fragmentView.id, RegisterStartFragment()).commit()
    }
    private fun initBottomNav() {

        binding.mainLayoutBottomNavigation.setOnItemSelectedListener {
            Log.d(TAG, "initBottomNav: ${it.itemId}")
            when (it.itemId) {
                R.id.home -> {
                }

                R.id.map -> {

                }

                R.id.mountain -> {

                }

                R.id.group -> {
                    changeFragment(GroupTabFragment())
                }

                R.id.mypage -> {

                }

            }
            return@setOnItemSelectedListener true
        }
    }
    private fun changeFragment(view: Fragment){
        supportFragmentManager.beginTransaction().replace(binding.fragmentView.id, view).commit()
    }
}