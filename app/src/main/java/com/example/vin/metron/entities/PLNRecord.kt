package com.example.vin.metron.entities

import com.google.firebase.Timestamp

data class PLNRecord(
    var no_pln: String?,
    var time_start: Timestamp?,
    var time_end: Timestamp?,
    var number_read: Double?,
    var usage: Double?
)
