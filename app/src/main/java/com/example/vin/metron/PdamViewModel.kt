package com.example.vin.metron

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vin.metron.entities.PDAMRecord
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class PdamViewModel: ViewModel() {

    fun getPDAMRecords(noPdam: String?): LiveData<ArrayList<PDAMRecord>>{
        val recordsResult = MutableLiveData<ArrayList<PDAMRecord>>()
        val db = FirebaseFirestore.getInstance()
        db.collection("records_pdam")
            .whereEqualTo("no_pdam", noPdam)
            .orderBy("time_end", Query.Direction.DESCENDING).get()
            .addOnSuccessListener { qs ->
                val records = ArrayList<PDAMRecord>()
                if(qs.documents.size != 0){
                    for(document in qs.documents){
                        val record = PDAMRecord(
                            document.get("no_pdam").toString(),
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

        return recordsResult
    }

    fun getPreviousRecord(noPdam: String?): LiveData<PDAMRecord?> {
        val pdamResult = MutableLiveData<PDAMRecord?>()
        val db = FirebaseFirestore.getInstance()
        db.collection("records_pdam")
            .whereEqualTo("no_pdam", noPdam)
            .orderBy("time_end", Query.Direction.DESCENDING).limit(1)
            .get()
            .addOnSuccessListener {
                if(it.documents.size == 0){
                    pdamResult.value = null
                } else{
                    pdamResult.value = PDAMRecord(
                        it.documents[0].get("no_pdam").toString(),
                        it.documents[0].get("time_start") as Timestamp,
                        it.documents[0].get("time_end") as Timestamp,
                        it.documents[0].get("number_read") as Double,
                        it.documents[0].get("usage") as Double
                    )
                }
            }

        return pdamResult
    }
}