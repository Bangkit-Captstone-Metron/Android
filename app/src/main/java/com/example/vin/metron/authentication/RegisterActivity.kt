package com.example.vin.metron.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.vin.metron.MainActivity
import com.example.vin.metron.R
import com.example.vin.metron.databinding.ActivityRegisterBinding
import com.example.vin.metron.entities.User
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
                val name = binding.nameET.text.toString()
                val noPln = binding.plnNoET.text.toString()
                val noPdam = binding.pdamNoET.text.toString()
                val phone = binding.phoneET.text.toString()
                val password = binding.passwordET.text.toString()
                registerUser(email, name, noPln, noPdam, phone, password)
            }
        }
    }

    private fun registerUser(email: String, name: String, noPln: String,
                             noPdam: String, phone: String, password: String){
        fAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("metron1", "registered successful")
                    val db = FirebaseFirestore.getInstance()
                    val user = User(email, name, noPln, noPdam, phone, password)
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