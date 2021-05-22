package com.example.vin.metron

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Camera
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.vin.metron.databinding.ContentTabBinding
import com.example.vin.metron.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment(), View.OnClickListener{
    companion object{
        private val REQ_CAMERA = 0

        private val TAB_TITLES = intArrayOf(
            R.string.pln,
            R.string.pdam
        )

        private val TAB_ICONS = intArrayOf(
            R.drawable.pln_logo,
            R.drawable.pdam_logo
        )
    }

    private lateinit var binding: FragmentHomeBinding
    private lateinit var contentTabBinding: ContentTabBinding
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTabs()
        homeViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(HomeViewModel::class.java)

        binding.cameraBtn.setOnClickListener(this)
    }

    private fun setTabs(){
        val sectionPagerAdapter = SectionPagerAdapter(activity as AppCompatActivity)
        binding.viewPagerVP.adapter = sectionPagerAdapter
        TabLayoutMediator(binding.tabsTL, binding.viewPagerVP){ tab, position ->
            contentTabBinding = ContentTabBinding.inflate(layoutInflater)
            Glide.with(this)
                .load(TAB_ICONS[position])
                .apply(RequestOptions().override(64, 64))
                .into(contentTabBinding.entLogoSIV)
            contentTabBinding.entNameTV.text= resources.getString(TAB_TITLES[position])

            tab.customView = contentTabBinding.root
        }.attach()
    }

    override fun onClick(view: View) {
        val intentToCamera = Intent(activity, CameraActivity::class.java)
        startActivityForResult(intentToCamera, REQ_CAMERA)
    }

}