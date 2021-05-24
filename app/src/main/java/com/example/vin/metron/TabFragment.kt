package com.example.vin.metron

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.vin.metron.databinding.FragmentTabBinding
import com.example.vin.metron.HomeFragment.Companion.TAB_TITLES

class TabFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentTabBinding

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
        val intentToCamera = Intent(context, CameraActivity::class.java)
        startActivity(intentToCamera)
    }
}