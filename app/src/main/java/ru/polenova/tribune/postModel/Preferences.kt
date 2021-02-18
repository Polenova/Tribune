package ru.polenova.tribune.postModel

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.Settings.Global.putString
import androidx.core.content.edit
import ru.polenova.tribune.PostActivity
import ru.polenova.tribune.api.Token

private const val TOKEN_KEY = "TOKEN_KEY"
private const val ATTACH_MODEL_KEY = "ATTACH_MODEL_KEY"
private const val FIRST_TIME_SHARED_KEY = "first_time_shared_key"
private const val SHARED_PREF_KEY = "SHARED_PREF"

fun savedToken(token: Token?, context: Context) {
    val sharedPref = context.getSharedPreferences(
        SHARED_PREF_KEY,
        Context.MODE_PRIVATE
    )
    sharedPref.edit {
        putString(
            TOKEN_KEY,
            token?.token
        )
    }
}

fun getToken(context: Context): String? {
    val sharedPref = context.getSharedPreferences(
        SHARED_PREF_KEY,
        Context.MODE_PRIVATE
    )
    return sharedPref.getString(
        TOKEN_KEY,
        ""
    )
}

fun isAuthorized(context: Context) {
    val sharedPref = context.getSharedPreferences(
        SHARED_PREF_KEY,
        Context.MODE_PRIVATE
    )
    val token = sharedPref.getString(
        TOKEN_KEY,
        ""
    )
    if (token != "") {
        val intent = Intent(context, PostActivity::class.java)
        context.startActivity(intent)
        val activity = context as Activity
        activity.finish()
    }
}

fun isFirstTime(context: Context) =
    context.getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE).getBoolean(
        FIRST_TIME_SHARED_KEY, true
    )

fun setNotFirstTime(context: Context) =
    context.getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE)
        .edit()
        .putBoolean(SHARED_PREF_KEY, false)