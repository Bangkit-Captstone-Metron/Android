package com.example.vin.metron

import android.content.Context
import com.example.vin.metron.entities.User
import com.google.firebase.firestore.QueryDocumentSnapshot

class UserPreferences(context: Context) {
    companion object{
        const val NAME_PREFS = "user preferences"
        const val EMAIL = "email"
        const val NAME = "name"
        const val NO_PLN = "no_pln"
        const val NO_PDAM = "no_pdam"
        const val PHONE = "phone"
    }

    private val preferences = context.getSharedPreferences(NAME_PREFS, Context.MODE_PRIVATE)

    fun setUser(document: QueryDocumentSnapshot){
        val editor = preferences.edit()
        editor.putString(EMAIL, document.get("email").toString())
        editor.putString(NAME, document.get("name").toString())
        editor.putString(NO_PLN, document.get("no_pln").toString())
        editor.putString(NO_PDAM, document.get("no_pdam").toString())
        editor.putString(PHONE, document.get("phone").toString())
        editor.commit()
    }

    fun getUser(): User {
        val user = User(
            preferences.getString(EMAIL, ""),
            preferences.getString(NAME, ""),
            preferences.getString(NO_PLN, ""),
            preferences.getString(NO_PDAM, ""),
            preferences.getString(PHONE, ""),
            null
        )

        return user
    }
}

