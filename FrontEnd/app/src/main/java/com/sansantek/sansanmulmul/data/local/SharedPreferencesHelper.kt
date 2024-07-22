package com.sansantek.sansanmulmul.data.local

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesUtil (context: Context) {
    private val SHARED_PREFERENCES_NAME = "sansanmulmul_preference"

    var preferences: SharedPreferences =
        context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

//    //사용자 정보 저장
//    fun addUser(user:User){
//        val editor = preferences.edit()
//        editor.putString("id", user.id)
//        editor.putString("name", user.name)
//        editor.apply()
//    }

//    fun getUser(): User{
//        val id = preferences.getString("id", "")
//        if (id != ""){
//            val name = preferences.getString("name", "")
//            return User(id!!, name!!, "",0)
//        }else{
//            return User()
//        }
//    }

//    fun deleteUser(){
//        //preference 지우기
//        val editor = preferences.edit()
//        editor.clear()
//        editor.apply()
//    }
}