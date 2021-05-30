package com.example.vin.metron

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.vin.metron.databinding.FragmentResultBinding


class ResultFragment : Fragment() {
    private lateinit var binding: FragmentResultBinding
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
    }


    fun setUIContent() {
        binding.ivResultFail.visibility = View.GONE
        binding.ivResultSucess.visibility = View.GONE
        binding.tvDesc.text = ""
        binding.tvUsage.text = ""

        val type = if (arguments?.getString(TabFragment.TYPE) == resources.getString(R.string.pdam)) "air" else "listrik"
        val isSuccess = arguments?.getBoolean(TabFragment.RESULT,false) ?: false
        val usage = arguments?.getFloat(TabFragment.USAGE)
        if (isSuccess) {
            binding.ivResultSucess.visibility = View.VISIBLE
            binding.tvDesc.text = "Data penggunaan $type berhasil tersimpan"
            binding.tvUsage.text = "$usage kw/h"
        } else {
            binding.ivResultFail.visibility = View.VISIBLE
            binding.tvDesc.text = "Penyimpanan gagal"
            Toast.makeText(
                context,
                "Pastikan Anda menggunakan foto meteran anda",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    //Todo: Backbutton, Animation, request penyimpanan data di halaman ini (?)

}