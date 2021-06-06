package com.example.vin.metron.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.example.vin.metron.R
import com.example.vin.metron.authentication.LoginActivity
import com.example.vin.metron.data.remote.ResultViewModel
import com.example.vin.metron.databinding.FragmentHomeBinding
import com.example.vin.metron.databinding.FragmentProfileBinding
import com.example.vin.metron.home.HomeViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class ProfileFragment : Fragment(),View.OnClickListener {
    private lateinit var binding: FragmentProfileBinding
    private val profileViewModel: ProfileViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        binding.btnLogout.setOnClickListener(this)
        binding.smNotif.setOnClickListener(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileViewModel.setUser(context=requireContext())
        getUserData()
    }

    fun getUserData(){
        profileViewModel.getUser().observe(viewLifecycleOwner){user ->
            Log.d("user_data",user.toString())
            binding.tvUsername.text = user.name
            binding.tvEmail.text = user.email
            binding.tvPdamId.text = resources.getString(R.string.profile_pdam_id,user.no_pdam)
            binding.tvPlnId.text = resources.getString(R.string.profile_pln_id,user.no_pln)
            binding.tvPhone.text = user.phone
            binding.smNotif.isChecked = user.isReminderNotifEnable
        }
    }

    override fun onClick(v: View?) {
        when (v?.id){
            R.id.btn_logout->{
                Firebase.auth.signOut()
                Log.d("metron1", "current user after signed out: ${Firebase.auth.currentUser}")
                val intent = Intent(requireContext(), LoginActivity::class.java)
                startActivity(intent)
            }
            R.id.sm_notif->{
                profileViewModel.toggleReminderMode(requireContext())
            }
        }
    }

}