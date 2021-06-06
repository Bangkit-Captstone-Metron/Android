package com.example.vin.metron.profile

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vin.metron.UserPreferences
import com.example.vin.metron.authentication.LoginActivity
import com.example.vin.metron.entities.User
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ProfileViewModel : ViewModel() {
    private var user: MutableLiveData<User> = MutableLiveData()

    fun setUser(context: Context) {
        val userPref = UserPreferences(context).getUser()
        if (userPref == null) {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }
        user.value = userPref
    }


    fun getUser(): LiveData<User> = user

    fun toggleReminderMode(context:Context) {
        val currentMode = user.value?.isReminderNotifEnable
        val userPreferences = UserPreferences(context)

        if (currentMode != null) {
            if (currentMode) {
                MaterialAlertDialogBuilder(context)
                    .setTitle("Menonaktifkan Notifikasi Pengingat")
                    .setMessage("Anda tidak akan menerima notifikasi pengingat bulanan untuk mengupload meteran jika menu ini dinonaktifkan. Lanjutkan ?")
                    .setNegativeButton("Batal") { _,_ ->
                    }
                    .setPositiveButton("Ya, Nonaktifkan saja") { _,_ ->
                        user.value?.isReminderNotifEnable = false
                        userPreferences.setReminderMode(isEnable = false)
                        Toast.makeText(context,"Peringatan bulanan dinonaktifkan",Toast.LENGTH_SHORT).show()
                    }
                    .show()
            } else {
                user.value?.isReminderNotifEnable = true
                userPreferences.setReminderMode(isEnable = true)
                Toast.makeText(context,"Peringatan bulanan diaktifkan",Toast.LENGTH_SHORT).show()
            }
        }
    }
}