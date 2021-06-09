package com.example.vin.metron.home

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.vin.metron.R
import com.example.vin.metron.UserPreferences
import com.example.vin.metron.databinding.FragmentHomeBinding

class HomeFragment : Fragment(),View.OnClickListener{
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            laporPlnLayout.setOnClickListener(this@HomeFragment)
            laporPdamLayout.setOnClickListener(this@HomeFragment)
            binding.btnToPln.setOnClickListener(this@HomeFragment)
            binding.btnToPdam.setOnClickListener(this@HomeFragment)
        }
        val name = context?.getSharedPreferences(UserPreferences.NAME_PREFS, Context.MODE_PRIVATE)?.getString(
            UserPreferences.NAME,"Pengguna"
        )
        binding.tvWelcome.text = resources.getString(R.string.welcome,name)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.laporPlnLayout-> findNavController().navigate(R.id.action_navigation_home_to_plnMainFragment)
            R.id.laporPdamLayout-> findNavController().navigate(R.id.action_navigation_home_to_pdamMainFragment)
            R.id.btn_to_pln -> findNavController().navigate(R.id.action_navigation_home_to_plnMainFragment)
            R.id.btn_to_pdam -> findNavController().navigate(R.id.action_navigation_home_to_pdamMainFragment)
        }
    }

    companion object{
        val TAB_TITLES = intArrayOf(
            R.string.pln,
            R.string.pdam
        )

        val TAB_ICONS = intArrayOf(
            R.drawable.logo_pln,
            R.drawable.logo_pdam
        )
    }

}