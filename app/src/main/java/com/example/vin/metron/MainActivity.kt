package com.example.vin.metron

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.vin.metron.authentication.LoginActivity
import com.example.vin.metron.databinding.ActivityMainBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var mainActivityBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivityBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainActivityBinding.root)

        setBottomNav()
        Log.d("metron1", "current user in main act: ${Firebase.auth.currentUser}")
    }

    private fun setBottomNav(){
        val navController = findNavController(R.id.navigation_host)
        val appBarConfiguration = AppBarConfiguration.Builder(setOf(
            R.id.navigation_home, R.id.navigation_history, R.id.navigation_profile
        )).build()

        setupActionBarWithNavController(navController, appBarConfiguration)
        mainActivityBinding.bottomNavView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.settings -> {
                //TODO: go to settings activity
                true
            }
            R.id.logout -> {
                Firebase.auth.signOut()
                Log.d("metron1", "current user after signed out: ${Firebase.auth.currentUser}")
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                true
            }
            else -> true
        }
    }
}