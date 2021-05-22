package com.example.vin.metron

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.vin.metron.databinding.ActivityCameraBinding

class CameraActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCameraBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        
    }
}