package com.wayproject.bwamov.utils         //package untuk membuat user tidak melakukan login berulang kali

import android.content.Context
import android.content.SharedPreferences

class Preferences (val context: Context){
        companion object{
            const val USER_PREFF = "User_PREFF"     //constant variabel aplikasi
        }

    var sharedPreferences = context.getSharedPreferences(USER_PREFF, 0) //function untuk membuat share preference

    fun setValues(key : String, value : String){                        //set data
        val editor:SharedPreferences.Editor = sharedPreferences.edit()  //untuk izin edit data
        editor.putString(key, value)
        editor.apply()
    }

    fun getValues(key : String) : String? {                              // untuk mengambil data
        return sharedPreferences.getString(key, "")
    }
}