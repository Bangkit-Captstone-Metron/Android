package com.example.vin.metron.data.remote

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.vin.metron.PREF_KEY
import com.example.vin.metron.PREF_LAST_PDAM_SUBMISSION
import com.example.vin.metron.PREF_LAST_PLN_SUBMISSION
import com.example.vin.metron.data.remote.network.ApiConfig
import com.example.vin.metron.data.remote.network.ApiService
import java.text.SimpleDateFormat
import java.util.*

class ResultViewModel : ViewModel() {
    private val apiService: ApiService = ApiConfig.provideApiService()

    //Todo: Create,Update operation for DB
    fun savePLNData() {}

    fun savePDAMData() {}

    fun getUpdatedOrNewlyCreatedAlarmSchedule(context: Context, isPLN: Boolean): Calendar {
        //Todo: contain logic bug, fix this
        val valKey = if (isPLN) PREF_LAST_PLN_SUBMISSION else PREF_LAST_PDAM_SUBMISSION
        val sharePref = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
        val lastSubmissionPref = sharePref.getString(valKey,null)
        var schedule: Calendar = Calendar.getInstance()
        val formatter = SimpleDateFormat("YYYY-MM-dd")
        Log.d("result page share pref",lastSubmissionPref.toString())
//        if (lastSubmissionPref != null) schedule.time = formatter.parse(lastSubmissionPref)
//        schedule.add(Calendar.DAY_OF_YEAR, 30)
        val newValue = formatter.format(schedule.time)

        //Update the sharedPreference
        val editor = sharePref.edit()
        editor.putString(valKey, newValue)
        editor.apply()
        return schedule
    }

}