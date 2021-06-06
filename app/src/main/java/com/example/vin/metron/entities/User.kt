package com.example.vin.metron.entities

data class User(
    var email: String?,
    var name: String?,
    var no_pln: String?,
    var no_pdam: String?,
    var phone: String?,
    var password: String?,
    var isReminderNotifEnable:Boolean = true,
)
