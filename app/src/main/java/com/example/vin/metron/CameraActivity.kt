package com.example.vin.metron

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.vin.metron.databinding.ActivityCameraBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraActivity : AppCompatActivity(), View.OnClickListener {
    companion object{
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUIRED_PERMISSIONS_CODE = 100
        private const val FILE_NAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
    }

    private lateinit var binding: ActivityCameraBinding
    private lateinit var executor: ExecutorService
    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        if(allPermissionGranted()){
            startCamera()
        }else{
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUIRED_PERMISSIONS_CODE)
        }

        executor = Executors.newSingleThreadExecutor()
        outputDirectory = getOutputDirectory()

        binding.captureBtn.setOnClickListener(this)
    }

    private fun allPermissionGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == REQUIRED_PERMISSIONS_CODE){
            if(allPermissionGranted()){
                startCamera()
            }
            else{
                Toast.makeText(this, "Camera permission is not granted", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startCamera(){
        val cameraProvideFuture = ProcessCameraProvider.getInstance(this)
        cameraProvideFuture.addListener(
            {
                val cameraProvider = cameraProvideFuture.get()
                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(binding.previewPV.surfaceProvider)
                }
                imageCapture = ImageCapture.Builder().build()
                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
                }catch (e: Exception){
                    Toast.makeText(this, "Camera fail to bind", Toast.LENGTH_SHORT).show()
                }
            }
        , ContextCompat.getMainExecutor(this))
    }

    override fun onClick(view: View?) {
        val imageCapture = imageCapture ?: return
        val photoFile = File(outputDirectory, SimpleDateFormat(FILE_NAME_FORMAT, Locale.US).format(System.currentTimeMillis()) + ".jpg")
        val outputOption = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(outputOption, ContextCompat.getMainExecutor(this), object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                Toast.makeText(this@CameraActivity, "Successfully save image", Toast.LENGTH_SHORT).show()
            }

            override fun onError(exception: ImageCaptureException) {
                Toast.makeText(this@CameraActivity, "Fail to save image", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun getOutputDirectory(): File{
        val directory = externalMediaDirs.firstOrNull()?.let {
            File(it, "Metron").apply { mkdirs() }
        }

        return if(directory != null && directory.exists()){
            directory
        } else{
            filesDir
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        executor.shutdown()
    }
}