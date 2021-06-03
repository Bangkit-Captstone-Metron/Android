package com.example.vin.metron

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.vin.metron.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity(), View.OnClickListener{
    private lateinit var binding: ActivityLoginBinding
    private lateinit var fAuth: FirebaseAuth
    private lateinit var mainIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registrationBtn.setOnClickListener(this@LoginActivity)

        binding.loginBtn.setOnClickListener(this)

        fAuth = Firebase.auth
        val currentUser = fAuth.currentUser
        mainIntent = Intent(this, MainActivity::class.java)
        Log.d("metron1", "current user in login act: $currentUser")
        if(currentUser != null){
            Log.d("metron1", "User $currentUser has logged in")
            startActivity(mainIntent)
        }
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.loginBtn -> {
                val email = binding.emailET.text.toString()
                val password = binding.passwordET.text.toString()
                fAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val db = FirebaseFirestore.getInstance()
                            val query = db.collection("users")
                                .whereEqualTo("email", email)
                            val result = query.get().addOnSuccessListener {documents ->
                                for(document in documents){
                                    Log.d("metron1",document.data.toString())
                                }
                            }

                            startActivity(mainIntent)
                        }else{
                            Toast.makeText(this, "Login failed, please try again", Toast.LENGTH_SHORT).show()
                            Log.d("metron1", "${task.exception}")
                        }
                    }
            }

            R.id.registrationBtn -> {
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            }
        }
    }
}