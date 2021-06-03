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
import com.example.vin.metron.entities.PLNRecord
import com.example.vin.metron.home.TabFragment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ResultFragment : Fragment() {
    private lateinit var binding: FragmentResultBinding
    private val alarmReceiver: AlarmReceiver = AlarmReceiver()
    private val resultViewModel: ResultViewModel by viewModels()
    private lateinit var userPreferences: UserPreferences

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
        val isFake = arguments?.getBoolean(TabFragment.RESULT, true) ?: false
        Log.d("result page",isFake.toString())
        val numberRead = arguments?.getFloat(TabFragment.NUMBER_READ)
        try {
            if (isFake) {
                saveToDatabase(numberRead, isPLN)
                //Todo: Save to Firestore DB
                scheduleAlarm(isPLN = isPLN)
                binding.ivResultSucess.visibility = View.VISIBLE
                binding.btnBack.visibility = View.VISIBLE
                binding.tvDesc.text = "Data penggunaan $type berhasil tersimpan"
                binding.tvUsage.text = "$numberRead kw/h"
            } else throw Exception("Gagal, Gunakan foto yang asli meteran anda yang jelas")
        } catch (e: Exception) {
            binding.ivResultFail.visibility = View.VISIBLE
            binding.tvDesc.text = "Penyimpanan gagal"
            binding.btnBack.visibility = View.VISIBLE


            Log.d("result page",e.message.toString())
            Toast.makeText(
                context,
                "ERROR: ${e.message}",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    private fun scheduleAlarm(isPLN: Boolean) {
        val schedule = resultViewModel.getUpdatedOrNewlyCreatedAlarmSchedule(
            context = requireContext(),
            isPLN = isPLN
        )
        Log.d("result page",schedule.toString())
        alarmReceiver.setAlarm(context = requireContext(), schedule=schedule, isPLN = isPLN)
    }

    private fun saveToDatabase(numberRead: Float?, isPLN: Boolean){
        Log.d("metron1", userPreferences.toString())
        val user = userPreferences.getUser()
        val db = FirebaseFirestore.getInstance()

        when(isPLN){
            true -> {
                val record = PLNRecord(user.no_pln, null, null, numberRead, getUsage(numberRead, isPLN, user.no_pln))
                db.collection("records_pln")
                    .add(record)
                    .addOnSuccessListener { documentReference ->
                        Log.d("metron1", "Record added")
                    }
                    .addOnFailureListener { e ->
                        Log.d("metron1", "Fail to add record with error $e")
                    }
            }
        }
    }

    private fun getUsage(numberRead: Float?, isPLN: Boolean, noPln: String?): Float? {
        var prevNumberRead: Float = 0.0F
        val db = FirebaseFirestore.getInstance()
        when (isPLN) {
            true -> {
                db.collection("records_pln")
                    .whereEqualTo("no_pln", noPln)
                    .orderBy("time_end", Query.Direction.DESCENDING).get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            prevNumberRead = if (document.exists()) {
                                Log.d("metron1", document.get("number_read").toString())
                                document.get("number_read").toString().toFloat()
                            } else {
                                Log.d("metron1", "no previous record")
                                0.0F
                            }
                            break
                        }
                    }
            }
            false -> {
                db.collection("records_pDAM")
                    .whereEqualTo("no_pdam", noPln)
                    .orderBy("time_end", Query.Direction.DESCENDING).get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            prevNumberRead = if (document.exists()) {
                                Log.d("metron1", document.get("number_read").toString())
                                document.get("number_read").toString().toFloat()
                            } else {
                                Log.d("metron1", "no previous record")
                                0.0F
                            }
                            break
                        }
                    }
            }
        }
        return if (numberRead != null) {
            numberRead-prevNumberRead
        } else{
            null
        }
    }

    private fun showToast(message: String){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}