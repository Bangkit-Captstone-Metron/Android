package com.example.vin.metron

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.vin.metron.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var fAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registerBtn.setOnClickListener(this)

        fAuth = Firebase.auth
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.registerBtn -> {
                val email = binding.emailET.text.toString()
                val password = binding.passwordET.text.toString()
                fAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("metron1", "registered successful")
                            val db = FirebaseFirestore.getInstance()
                            val user = hashMapOf(
                                "nik" to binding.nikET.text.toString(),
                                "name" to binding.nameET.text.toString(),
                                "phone" to binding.phoneET.text.toString(),
                                "email" to binding.emailET.text.toString(),
                                "password" to binding.passwordET.text.toString()
                            )
                            db.collection("users")
                                .add(user)
                                    .addOnSuccessListener { documentReference ->
                                        val intent = Intent(this, MainActivity::class.java)
                                        startActivity(intent)
                                        Log.d("metron1", "Register succeed")
                                    }
                                    .addOnFailureListener { e ->
                                        Log.d("metron1", "Fail to add user data")
                                    }
                        }else{
                            Toast.makeText(this, "Register failed, please try again", Toast.LENGTH_SHORT).show()
                            Log.d("metron1", "${task.exception}")
                        }
                    }
            }
        }
    }
}