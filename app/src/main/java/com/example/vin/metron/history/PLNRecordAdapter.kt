package com.example.vin.metron.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.vin.metron.R
import com.example.vin.metron.databinding.ItemRecordBinding
import com.example.vin.metron.entities.PLNRecord
import java.text.SimpleDateFormat

class PLNRecordAdapter(private val records: ArrayList<PLNRecord>): RecyclerView.Adapter<PLNRecordAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(records[position])
    }

    override fun getItemCount() = records.size

    class ViewHolder(private val binding: ItemRecordBinding): RecyclerView.ViewHolder(binding.root){
        val formatter = SimpleDateFormat("dd-MM-yyyy")

        fun bind(record: PLNRecord){
            binding.apply {
                noPlnTV.text = "No.${record.no_pln}"
                usageTV.text = "${record.usage} Kw/H"
                timeStartTV.text =
                    "Awal pemakaian: ${formatter.format(record.time_start?.toDate())}"
                timeEndTV.text =
                    "Akhir pemakaian: ${formatter.format(record.time_end?.toDate())}"
                Glide.with(root.context)
                    .load(R.drawable.pln_logo)
                    .apply(RequestOptions().override(85, 85))
                    .into(logoIV)
            }
        }
    }
}