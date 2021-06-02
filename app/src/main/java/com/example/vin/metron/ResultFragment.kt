package com.example.vin.metron

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.vin.metron.data.remote.ResultViewModel
import com.example.vin.metron.databinding.FragmentResultBinding
import java.util.*


class ResultFragment : Fragment() {
    private lateinit var binding: FragmentResultBinding
    private val alarmReceiver: AlarmReceiver = AlarmReceiver()
    private val resultViewModel: ResultViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentResultBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUIContent()
        backToHomeButtonListener()
    }

    fun backToHomeButtonListener(){
        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_resultFragment_to_navigation_home)
        }
    }
    fun setUIContent() {
        binding.ivResultFail.visibility = View.GONE
        binding.ivResultSucess.visibility = View.GONE
        binding.btnBack.visibility = View.GONE
        binding.tvDesc.text = ""
        binding.tvUsage.text = ""

        val isPLN = (arguments?.getString(TabFragment.TYPE) == resources.getString(R.string.pln))
        val type = if (isPLN) "listrik" else "air"
        val isSuccess = arguments?.getBoolean(TabFragment.RESULT, false) ?: false
        Log.d("result page",isSuccess.toString())
        val usage = arguments?.getFloat(TabFragment.USAGE)
        try {
            if (isSuccess) {
                //Todo: Save to Firestore DB
                scheduleAlarm(isPLN = isPLN)
                binding.ivResultSucess.visibility = View.VISIBLE
                binding.btnBack.visibility = View.VISIBLE
                binding.tvDesc.text = "Data penggunaan $type berhasil tersimpan"
                binding.tvUsage.text = "$usage kw/h"
            } else throw Exception("Gagal, Gunakan foto yang asli meteran anda yang jelas")
        } catch (e: Exception) {
            binding.ivResultFail.visibility = View.VISIBLE
            binding.tvDesc.text = "Penyimpanan gagal"
            binding.btnBack.visibility = View.VISIBLE


            Log.d("result page",e.message.toString())
            Toast.makeText(
                context,
                e.message,
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    fun scheduleAlarm(isPLN: Boolean) {
        val schedule = resultViewModel.getUpdatedOrNewlyCreatedAlarmSchedule(
            context = requireContext(),
            isPLN = isPLN
        )
        Log.d("result page",schedule.toString())
        alarmReceiver.setAlarm(context = requireContext(), schedule=schedule, isPLN = isPLN)
    }
}