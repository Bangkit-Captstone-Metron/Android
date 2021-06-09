package com.example.vin.metron

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot

class UserViewModel: ViewModel() {

    fun getUserDataByDocRef(document: DocumentReference): LiveData<DocumentSnapshot>{
        val docResult = MutableLiveData<DocumentSnapshot>()
        document
            .get()
            .addOnSuccessListener {
                docResult.value = it
            }
        return docResult
    }
}