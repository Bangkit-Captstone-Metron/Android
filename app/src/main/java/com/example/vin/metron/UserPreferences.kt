package com.example.vin.metron

import android.content.Context
import com.example.vin.metron.entities.User
import com.google.firebase.firestore.QueryDocumentSnapshot

class UserPreferences(context: Context) {
    companion object {
        const val NAME_PREFS = "user preferences"
        const val EMAIL = "email"
        const val NAME = "name"
        const val NO_PLN = "no_pln"
        const val NO_PDAM = "no_pdam"
        const val PHONE = "phone"
        const val ALLOW_REMINDER_NOTIF = "ALLOW_REMINDER_NOTIF"
    }

    private val preferences = context.getSharedPreferences(NAME_PREFS, Context.MODE_PRIVATE)

    fun setUser(document: QueryDocumentSnapshot) {
        val editor = preferences.edit()
        editor.putString(EMAIL, document.get("email").toString())
        editor.putString(NAME, document.get("name").toString())
        editor.putString(NO_PLN, document.get("no_pln").toString())
        editor.putString(NO_PDAM, document.get("no_pdam").toString())
        editor.putString(PHONE, document.get("phone").toString())
        if (!preferences.contains(ALLOW_REMINDER_NOTIF)) editor.putBoolean(ALLOW_REMINDER_NOTIF, true)
        editor.commit()
    }

    fun setReminderMode(isEnable:Boolean) {
        val editor = preferences.edit()
        editor.putBoolean(ALLOW_REMINDER_NOTIF, isEnable)
        editor.commit()
    }


    fun getUser(): User? {
        if (!preferences.contains(EMAIL)) return null
        val user = User(
            preferences.getString(EMAIL, ""),
            preferences.getString(NAME, ""),
            preferences.getString(NO_PLN, ""),
            preferences.getString(NO_PDAM, ""),
            preferences.getString(PHONE, ""),
            null,
            preferences.getBoolean(ALLOW_REMINDER_NOTIF, true)
        )
        return user
    }
}

