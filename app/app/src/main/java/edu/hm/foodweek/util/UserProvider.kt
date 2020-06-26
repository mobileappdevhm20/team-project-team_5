package edu.hm.foodweek.util

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings

class UserProvider(val context: Context) {
    var cache: String? = null

    @SuppressLint("HardwareIds")
    fun getUserID(): String {
        if (cache != null) {
            return cache!!
        }

        val id = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        )
        cache = id
        return id
    }
}