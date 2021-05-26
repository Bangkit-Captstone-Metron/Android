package com.example.vin.metron

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import com.example.vin.metron.databinding.FragmentTabBinding
import com.example.vin.metron.HomeFragment.Companion.TAB_TITLES
import com.theartofdev.edmodo.cropper.CropImage

class TabFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentTabBinding
    private val cropActivityResultContract = object : ActivityResultContract<Any?,Uri?>(){
        override fun createIntent(context: Context, input: Any?): Intent {
            return CropImage.activity().setAspectRatio(16,9).getIntent(context)
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
            //Todo: check for other type
            return CropImage.getActivityResult(intent)?.uri
        }
    }

    private var cropActivityResultLauncher: ActivityResultLauncher<Any?>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTabBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val resTitle = arguments?.getInt("section", TAB_TITLES[0]) as Int

        cropActivityResultLauncher = registerForActivityResult(cropActivityResultContract){
            it?.let{
                binding.photoIV.setImageURI(it)
            }
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
       when(view.id){
           R.id.cameraBtn -> {
               cropActivityResultLauncher?.launch(null)
           }
       }
    }
}