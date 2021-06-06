package com.example.vin.metron.info

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vin.metron.data.remote.dummy.News
import com.example.vin.metron.data.remote.dummy.NewsDummy
import com.example.vin.metron.databinding.FragmentInfoBinding

class InfoFragment : Fragment(), AdapterCallback {
    private lateinit var binding: FragmentInfoBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInfoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showDummyData()
        hotlineBtnsListener()
    }

    private fun showDummyData() {
        val list = NewsDummy.getDummyNews()
        val adapter = ListNewsAdapter(list, this)
        binding.rvNews.layoutManager = LinearLayoutManager(context)
        binding.rvNews.setHasFixedSize(true)
        binding.rvNews.adapter = adapter
    }

    override fun <T : Any> onButtonClick(data: T) {
        data as News
        val uri = Uri.parse(data.url)
        try {
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        } catch (e: Error) {
            Toast.makeText(context, "Gagal!! Silahkan coba lagi beberapa saat", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun hotlineBtnsListener() {
        binding.btnPln.setOnClickListener {
            val callIntent = Intent(Intent.ACTION_DIAL)
            callIntent.data = Uri.parse("tel:4579900")
            startActivity(callIntent)
        }
        binding.btnPdam.setOnClickListener {
            val callIntent = Intent(Intent.ACTION_DIAL)
            callIntent.data = Uri.parse("tel: 4571666")
            startActivity(callIntent)
        }
    }

}