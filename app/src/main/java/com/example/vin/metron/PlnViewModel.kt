package com.example.vin.metron

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vin.metron.entities.PLNRecord
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class PlnViewModel: ViewModel() {

    fun getPLNRecords(noPln: String?): LiveData<ArrayList<PLNRecord>>{
        val recordsResult = MutableLiveData<ArrayList<PLNRecord>>()
        val db = FirebaseFirestore.getInstance()
        db.collection("records_pln")
            .whereEqualTo("no_pln", noPln)
            .orderBy("time_end", Query.Direction.DESCENDING).get()
            .addOnSuccessListener { qs ->
                val records = ArrayList<PLNRecord>()
                for(document in qs.documents){
                    val record = PLNRecord(
                        document.get("no_pln").toString(),
                        document.get("time_start") as Timestamp,
                        document.get("time_end") as Timestamp,
                        document.get("number_read").toString().toFloat(),
                        document.get("usage").toString().toFloat()
                    )
                    records.add(record)
                }
                recordsResult.value = records
            }
            .addOnFailureListener{
                Log.d("metron1", "Fail to get PLN records with exception $it")
            }

        return recordsResult
    }

    fun getPreviousNumberRead(noPln: String?): LiveData<Float?> {
        val prevNumberReadResult = MutableLiveData<Float?>()
        val db = FirebaseFirestore.getInstance()
        db.collection("records_pln")
            .whereEqualTo("no_pln", noPln)
            .orderBy("time_end", Query.Direction.DESCENDING).limit(1)
            .get()
            .addOnSuccessListener {
                if(it.documents.size == 0){
                    prevNumberReadResult.value = 0.0f
                } else{
                    prevNumberReadResult.value = it.documents[0].get("number_read").toString().toFloatOrNull()
                    Log.d("metron1","success with doc: ${it.documents[0]}")
                }
            }
            .addOnFailureListener{
                Log.d("metron1", "Fail to get previous record from database")
            }

        return prevNumberReadResult
    }
}