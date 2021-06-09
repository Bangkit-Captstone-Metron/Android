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
                if(qs.documents.size != 0){
                    for(document in qs.documents){
                        val record = PLNRecord(
                            document.get("no_pln").toString(),
                            document.get("time_start") as Timestamp,
                            document.get("time_end") as Timestamp,
                            document.get("number_read").toString().toDouble(),
                            document.get("usage").toString().toDouble()
                        )
                        records.add(record)
                    }
                }
                recordsResult.value = records
            }
            .addOnFailureListener{
                Log.d("metron1", "Fail to get PLN records with exception $it")
            }

        return recordsResult
    }

    fun getPreviousRecord(noPln: String?): LiveData<PLNRecord?> {
        val plnResult = MutableLiveData<PLNRecord?>()
        val db = FirebaseFirestore.getInstance()
        db.collection("records_pln")
            .whereEqualTo("no_pln", noPln)
            .orderBy("time_end", Query.Direction.DESCENDING).limit(1)
            .get()
            .addOnSuccessListener {
                if(it.documents.size == 0){
                    plnResult.value = null
                } else{
                    plnResult.value = PLNRecord(
                        it.documents[0].get("no_pln").toString(),
                        it.documents[0].get("time_start") as Timestamp,
                        it.documents[0].get("time_end") as Timestamp,
                        it.documents[0].get("number_read") as Double,
                        it.documents[0].get("usage") as Double
                    )
                }
            }
            .addOnFailureListener{
                Log.d("metron1", "Fail to get previous record from database")
            }

        return plnResult
    }
}