package com.example.vin.metron.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.vin.metron.databinding.ContentTabBinding
import com.example.vin.metron.databinding.FragmentHistoryBinding
import com.example.vin.metron.home.HomeFragment
import com.example.vin.metron.home.SectionPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var contentTabBinding: ContentTabBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTabs()
    }

    private fun setTabs() {
        val historySectionPagerAdapter = HistorySectionPagerAdapter(activity as AppCompatActivity)
        binding.viewPagerVP.adapter = historySectionPagerAdapter
        TabLayoutMediator(binding.tabsTL, binding.viewPagerVP) { tab, position ->
            contentTabBinding = ContentTabBinding.inflate(layoutInflater)
            Glide.with(this)
                .load(HomeFragment.TAB_ICONS[position])
                .apply(RequestOptions().override(64, 64))
                .into(contentTabBinding.entLogoSIV)
            contentTabBinding.entNameTV.text = resources.getString(HomeFragment.TAB_TITLES[position])

            tab.customView = contentTabBinding.root
        }.attach()
    }
}