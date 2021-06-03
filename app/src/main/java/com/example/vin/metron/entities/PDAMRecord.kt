package com.example.vin.metron.entities

import com.google.firebase.Timestamp

data class PDAMRecord(
    var no_pdam: String?,
    var time_start: Timestamp?,
    var time_end: Timestamp?,
    var number_read: Float?,
    var usage: Float?
)
