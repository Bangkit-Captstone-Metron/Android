package com.example.vin.metron.result

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.vin.metron.PREF_KEY
import com.example.vin.metron.PREF_LAST_PDAM_SUBMISSION
import com.example.vin.metron.PREF_LAST_PLN_SUBMISSION
import java.text.SimpleDateFormat
import java.util.*

class ResultViewModel : ViewModel() {

    fun getUpdatedOrNewlyCreatedAlarmSchedule(context: Context, isPLN: Boolean): Calendar? {
        val valKey = if (isPLN) PREF_LAST_PLN_SUBMISSION else PREF_LAST_PDAM_SUBMISSION
        val sharePref = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
        val lastSubmissionPref = sharePref.getString(valKey,null)

        if (lastSubmissionPref != null) return null

        var schedule: Calendar = Calendar.getInstance()
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        schedule.add(Calendar.DAY_OF_YEAR, 30)
        val newValue = formatter.format(schedule.time)

        val editor = sharePref.edit()
        editor.putString(valKey, newValue)
        editor.apply()
        return schedule
    }

}