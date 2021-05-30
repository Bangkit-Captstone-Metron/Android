package com.example.vin.metron

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.vin.metron.databinding.ActivityResultBinding
import com.example.vin.metron.databinding.FragmentTabBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}