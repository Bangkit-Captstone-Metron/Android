package com.example.vin.metron.result

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.example.vin.metron.PdamViewModel
import com.example.vin.metron.PlnViewModel
import com.example.vin.metron.R
import com.example.vin.metron.UserPreferences
import com.example.vin.metron.databinding.FragmentResultBinding
import com.example.vin.metron.entities.PDAMRecord
import com.example.vin.metron.entities.PLNRecord
import com.example.vin.metron.home.TabFragment
import com.example.vin.metron.profile.AlarmReceiver
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore

class ResultFragment : Fragment() {
    private lateinit var binding: FragmentResultBinding
    private val alarmReceiver: AlarmReceiver =
        AlarmReceiver()
    private val resultViewModel: ResultViewModel by viewModels()
    private lateinit var userPreferences: UserPreferences
    private lateinit var plnViewModel: PlnViewModel
    private lateinit var pdamViewModel: PdamViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentResultBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userPreferences = UserPreferences(requireContext())
        plnViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.NewInstanceFactory()
        )[PlnViewModel::class.java]
        pdamViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.NewInstanceFactory()
        )[PdamViewModel::class.java]
        userPreferences = UserPreferences(requireContext())

        setUIContent()
        backToHomeButtonListener()
    }

    private fun backToHomeButtonListener() {
        binding.btnBack.setOnClickListener {
            requireActivity().finish()
//            TOdo: perform back button
//            findNavController().navigate(R.id.action_resultFragment_to_navigation_home)
        }
    }

    private fun setUIContent() {
        binding.apply {
            ivResultFail.visibility = View.GONE
            ivResultSucess.visibility = View.GONE
            btnBack.visibility = View.GONE
            tvDesc.text = ""
            tvUsage.text = ""
        }

        val isPLN = (arguments?.getString(TabFragment.TYPE) == resources.getString(
            R.string.pln
        ))
        val type = if (isPLN) "listrik" else "air"
        val isFake = arguments?.getBoolean(TabFragment.RESULT, true) ?: false
        val numberRead = arguments?.getFloat(TabFragment.NUMBER_READ)
        try {
            if (isFake) {
                saveToDatabase(numberRead, isPLN)
                scheduleAlarm(isPLN = isPLN)
                binding.apply {
                    ivResultSucess.visibility = View.VISIBLE
                    btnBack.visibility = View.VISIBLE
                    tvDesc.text = "Data penggunaan $type berhasil tersimpan"
                    tvUsage.text = "$numberRead kw/h"
                }
            } else throw Exception("Gagal, Gunakan foto yang asli meteran anda yang jelas")
        } catch (e: Exception) {
            binding.apply {
                ivResultFail.visibility = View.VISIBLE
                tvDesc.text = "Penyimpanan gagal"
                btnBack.visibility = View.VISIBLE
            }
            showToast("ERROR: ${e.message}")
        }

    }

    private fun scheduleAlarm(isPLN: Boolean) {
        val schedule = resultViewModel.getUpdatedOrNewlyCreatedAlarmSchedule(
            context = requireContext(),
            isPLN = isPLN
        )
        Log.d("result page", schedule.toString())
        alarmReceiver.setAlarm(context = requireContext(), schedule = schedule, isPLN = isPLN)
    }

    private fun saveToDatabase(numberRead: Float?, isPLN: Boolean) {
        val user = userPreferences.getUser()
        val db = FirebaseFirestore.getInstance()
        when (isPLN) {
            true -> {
                plnViewModel.getPreviousNumberRead(user?.no_pln)
                    .observe(viewLifecycleOwner) { prevNumberRead ->
                        val record = PLNRecord(
                            user?.no_pln,
                            Timestamp.now(),
                            Timestamp.now(),
                            numberRead,
                            (numberRead!! - prevNumberRead!!)
                        )
                        db.collection("records_pln")
                            .add(record)
                            .addOnSuccessListener {
                                Log.d("metron1", "Record added")
                            }
                            .addOnFailureListener { e ->
                                Log.d("metron1", "Fail to add record with error $e")
                            }
                    }
            }

            false -> {
                pdamViewModel.getPreviousNumberRead(user?.no_pdam)
                    .observe(viewLifecycleOwner) { prevNumberRead ->
                        val record = PDAMRecord(
                            user?.no_pdam,
                            Timestamp.now(),
                            Timestamp.now(),
                            numberRead,
                            (numberRead!! - prevNumberRead!!)
                        )
                        db.collection("records_pdam")
                            .add(record)
                            .addOnSuccessListener {
                                Log.d("metron1", "Record added")
                            }
                            .addOnFailureListener { e ->
                                Log.d("metron1", "Fail to add record with error $e")
                            }
                    }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}