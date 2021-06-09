package com.example.vin.metron.home

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.NavAction
import androidx.navigation.fragment.findNavController
import com.example.vin.metron.*
import com.example.vin.metron.databinding.FragmentPlnMainBinding
import com.example.vin.metron.result.ResultFragment
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizerOptions
import com.theartofdev.edmodo.cropper.CropImage
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class PlnMainFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentPlnMainBinding
    private var cropActivityResultLauncher: ActivityResultLauncher<Any?>? = null
    private var cropResultUri: Uri? = null
    private val homeViewModel: HomeViewModel by viewModels()
    private val cropActivityResultContract = object : ActivityResultContract<Any?, Uri?>() {
        override fun createIntent(context: Context, input: Any?): Intent {
            return CropImage.activity()
                .setFixAspectRatio(false)
                .getIntent(context)
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
            return CropImage.getActivityResult(intent)?.uri
        }
    }
    private var usage: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlnMainBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        resetUI()
        cropActivityResultLauncher = registerForActivityResult(cropActivityResultContract) {
            it?.let {
                binding.photoIV.setImageURI(it)
                binding.photoIV.visibility = View.VISIBLE
                cropResultUri = it
            }
            binding.progressBar.visibility = View.VISIBLE
            if (cropResultUri != null) extractText(imageUri = cropResultUri!!)
            else Toast.makeText(
                context,
                "Uri tidak ditemukan, silahkan foto atau upload ulang",
                Toast.LENGTH_SHORT
            ).show()
            binding.progressBar.visibility = View.GONE
        }
        binding.cameraBtn.setOnClickListener(this)
        binding.btnSubmit.setOnClickListener(this)
    }


    override fun onClick(view: View) {
        when (view.id) {
            R.id.cameraBtn -> {
                cropActivityResultLauncher?.launch(null)
            }
            R.id.btn_submit -> {
                Toast.makeText(context, "start submit", Toast.LENGTH_SHORT).show()
                try {
                    onSubmit()
                } catch (e: Error) {
                    Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun extractText(imageUri: Uri) {
        val image = InputImage.fromFilePath(context, imageUri)
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                try {
                    usage = postProcessingOCR(visionText)
                    binding.apply {
                        tvOcrResult.text = "$usage Kw/H"
                        btnSubmit.visibility = View.VISIBLE
                    }
                } catch (e: Error) {
                    Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
            }
    }

    private fun postProcessingOCR(ocrResult: Text): String {
        if (ocrResult.textBlocks.isEmpty()) throw Error("Data digit tidak terdeteksi.")
        val tempOutput = ocrResult.textBlocks[0].lines[0].text.replace(
            "\\s+",
            ""
        ).toLowerCase().trim()
        var outputString: String = ""

        for (i in tempOutput.indices) {
            if (!tempOutput[i].isDigit()) {
                when (tempOutput[i]) {
                    'o' -> outputString += "0"
                    'i' -> outputString += "1"
                    'e' -> outputString += "6"
                    'g' -> outputString += "9"
                }
            } else outputString += tempOutput[i]
        }
        Log.d("ocr size", outputString.trim().length.toString())
        when (outputString.trim().length) {
            5 -> outputString += ".5"
            6 -> outputString =
                outputString.substring(0, 5) + "." + outputString.substring(5, outputString.length)
            else -> throw Error("Data digit tidak terbaca lengkap")
        }
        return outputString
    }

    private fun resetUI() {
        with(binding) {
            tvOcrResult.text = ""
            btnSubmit.visibility = View.GONE
            photoIV.visibility = View.GONE
            cameraBtn.visibility = View.VISIBLE
        }
    }

    private fun onSubmit() {
        if (cropResultUri == null) throw Error("Gagal submit, uri tidak valid")
        binding.progressBar.visibility = View.VISIBLE
        val file = File(cropResultUri!!.path)
        val requestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val body: MultipartBody.Part = MultipartBody.Part.createFormData(
            name = "image",
            filename = file.name,
            body = requestBody
        )
        homeViewModel.checkIsFakeFromURI(body, requireContext()).observe(viewLifecycleOwner) {
            Log.d("metron1", "${it.isFake} and ${it.confidence}")
            if (it.isFake != null && it.confidence > 75.00) {
                val usage = usage
                val bundle = Bundle()
                bundle.putString(SERVICE_TYPE, PLN)
                bundle.putBoolean(IS_FAKE, it.isFake)
                bundle.putDouble(OCR_READING, usage.toDouble())

                findNavController().navigate(R.id.action_plnMainFragment_to_resultFragment, bundle)
            }
            binding.progressBar.visibility = View.GONE
        }
    }
}