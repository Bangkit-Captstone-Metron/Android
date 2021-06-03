package com.example.vin.metron.authentication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.vin.metron.MainActivity
import com.example.vin.metron.R
import com.example.vin.metron.UserPreferences
import com.example.vin.metron.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity(), View.OnClickListener{

    private lateinit var binding: ActivityLoginBinding
    private lateinit var fAuth: FirebaseAuth
    private lateinit var mainIntent: Intent
    private lateinit var userPreference: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registrationBtn.setOnClickListener(this@LoginActivity)

        binding.loginBtn.setOnClickListener(this)

        fAuth = Firebase.auth
        mainIntent = Intent(this, MainActivity::class.java)
        userPreference = UserPreferences(this)

        val currentUser = fAuth.currentUser
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
                loginUser(email, password)
            }
            R.id.registrationBtn -> {
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun loginUser(email: String, password: String){
        fAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val db = FirebaseFirestore.getInstance()
                    val result = db.collection("users")
                        .whereEqualTo("email", email)
                    result.get().addOnSuccessListener {documents ->
                        for(document in documents){
                            userPreference.setUser(document)
                            break
                        }
                    }

                    startActivity(mainIntent)
                }else{
                    Toast.makeText(this, "Login failed, please try again", Toast.LENGTH_SHORT).show()
                    Log.d("metron1", "${task.exception}")
                }
            }
    }
}