package com.example.vin.metron

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.fragment.app.Fragment
import com.example.vin.metron.HomeFragment.Companion.TAB_TITLES
import com.example.vin.metron.databinding.FragmentTabBinding
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.TextRecognizerOptions
import com.theartofdev.edmodo.cropper.CropImage


class TabFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentTabBinding
    private var cropResultUri: Uri? = null
    private val cropActivityResultContract = object : ActivityResultContract<Any?, Uri?>() {
        override fun createIntent(context: Context, input: Any?): Intent {
            return CropImage.activity()
                .setAspectRatio(18, 9)
                .setFixAspectRatio(false)
                .getIntent(context)
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
            return CropImage.getActivityResult(intent)?.uri
        }
    }

    private var cropActivityResultLauncher: ActivityResultLauncher<Any?>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTabBinding.inflate(layoutInflater)
        resetUI()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val resTitle = arguments?.getInt("section", TAB_TITLES[0]) as Int

        cropActivityResultLauncher = registerForActivityResult(cropActivityResultContract) {
            it?.let {
                binding.photoIV.setImageURI(it)
                cropResultUri = it
            }
            //Todo: Start Loading
            if (cropResultUri != null) extractText(imageUri = cropResultUri!!)
            else Toast.makeText(context, "Error!!!, Silahkan upload foto ulang", Toast.LENGTH_SHORT)
            //Todo: Stop Loading
        }
        binding.titleTV.text = resources.getString(resTitle)
        binding.cameraBtn.setOnClickListener(this)

    }

    companion object {
        @JvmStatic
        fun newInstance(index: Int) =
            TabFragment().apply {
                arguments = Bundle().apply {
                    putInt("section", TAB_TITLES[index])
                }
            }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.cameraBtn -> {
                cropActivityResultLauncher?.launch(null)
            }
        }
    }

    fun extractText(imageUri: Uri) {
        val image = InputImage.fromFilePath(context, imageUri)
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        val result = recognizer.process(image)
            .addOnSuccessListener { visionText ->
                Log.d("ocr raw",visionText.text)
                val result = postProcessingOCR(visionText)
                Log.d("ocr processed",result)
                binding.tvOcrResult.text = "$result Kw/H"
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT)
            }
    }

    fun resetUI() {
        binding.tvOcrResult.text = ""
        binding.photoIV.setImageResource(R.drawable.ic_baseline_image_24)
    }

    fun postProcessingOCR(ocrResult: Text): String {
        //Todo: research more about ocr postprocessing
        val tempOutput = ocrResult.textBlocks[0].lines[0].text.replace(
            "\\s+",
            ""
        ).toLowerCase().trim()
        Log.d("ocr temp",tempOutput.toString())
        var outputString: String =""

        for (i in tempOutput.indices){
            if (!tempOutput[i].isDigit()){
                when(tempOutput[i]){
                    'o' -> outputString += "0"
                    'i' -> outputString += "1"
                    'e' -> outputString += "6"
                    'g' -> outputString += "9"
                }
            } else outputString += tempOutput[i]
        }
        Log.d("ocr size",outputString.trim().length.toString())
        if (outputString.trim().length == 5) outputString += "5"
        return outputString
    }
}