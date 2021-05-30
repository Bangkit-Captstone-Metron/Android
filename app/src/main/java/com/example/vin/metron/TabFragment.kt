package com.example.vin.metron

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.vin.metron.HomeFragment.Companion.TAB_TITLES
import com.example.vin.metron.databinding.FragmentTabBinding
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizerOptions
import com.theartofdev.edmodo.cropper.CropImage
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.MultipartBody.Part.Companion.createFormData
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


class TabFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentTabBinding
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
            Log.d("ocr uri", cropResultUri.toString())
            if (cropResultUri != null) extractText(imageUri = cropResultUri!!)
            else Toast.makeText(
                context,
                "Uri tidak ditemukan, silahkan foto atau upload ulang",
                Toast.LENGTH_SHORT
            ).show()
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
            R.id.btn_submit -> {
                Toast.makeText(context,"start submit",Toast.LENGTH_SHORT).show()
                Log.d("res","start submit")
                try {
                    onSubmit()
                } catch(e: Error){
                    Toast.makeText(context,e.message.toString(),Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun extractText(imageUri: Uri) {
        val image = InputImage.fromFilePath(context, imageUri)
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        val result = recognizer.process(image)
            .addOnSuccessListener { visionText ->
                Log.d("ocr raw", visionText.text)
                try {
                    val result = postProcessingOCR(visionText)
                    binding.tvOcrResult.text = "$result Kw/H"
                    binding.btnSubmit.visibility = View.VISIBLE
                } catch (e: Error) {
                    Log.d("ocr fail", "test")
                    Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
            }
    }

    fun resetUI() {
        with(binding) {
            tvOcrResult.text = ""
            photoIV.setImageResource(R.drawable.ic_baseline_image_24)
            btnSubmit.visibility = View.GONE
        }
    }

    fun postProcessingOCR(ocrResult: Text): String {
        //Todo: research more about ocr postprocessing
        if (ocrResult.textBlocks.isEmpty()) throw Error("Data digit tidak terdeteksi. Silahkan foto ulang")
        val tempOutput = ocrResult.textBlocks[0].lines[0].text.replace(
            "\\s+",
            ""
        ).toLowerCase().trim()
        Log.d("ocr temp", tempOutput.toString())
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
        if (outputString.trim().length == 5) outputString += "5"
        return outputString
    }

    fun onSubmit() {
        if (cropResultUri == null) throw Error("Gagal submit, uri tidak valid")
        //Todo : Start loading, move
        val file = File(cropResultUri!!.path)
        val requestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val body: MultipartBody.Part = MultipartBody.Part.createFormData(
            name = "image",
            filename = file.name,
            body = requestBody
        )
        val res = homeViewModel.checkIsFake(body).value?.isFake
        Log.d("res",res.toString())
    }
}