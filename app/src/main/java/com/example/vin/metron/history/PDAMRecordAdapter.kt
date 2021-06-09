package com.example.vin.metron.history

import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.text.style.SuperscriptSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.vin.metron.R
import com.example.vin.metron.databinding.ItemRecordBinding
import com.example.vin.metron.entities.PDAMRecord
import java.text.SimpleDateFormat

class PDAMRecordAdapter(private val records: ArrayList<PDAMRecord>): RecyclerView.Adapter<PDAMRecordAdapter.ViewHolder>() {
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

        val text = "m3"
        val ssBuilder = SpannableStringBuilder(text)
        val superscriptSpan = SuperscriptSpan()
        fun bind(record: PDAMRecord){
            ssBuilder.setSpan(
                superscriptSpan,
                text.indexOf("3"),
                text.indexOf("3") + 1,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            ssBuilder.setSpan(
                RelativeSizeSpan(.5f),
                text.indexOf("3"),
                text.indexOf("3") + 1,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            binding.apply {
                noPlnTV.text = "No.${record.no_pdam}"
                usageTV.text = "${record.usage} $ssBuilder"
                timeStartTV.text =
                    "Awal pengunaan: ${formatter.format(record.time_start?.toDate())}"
                timeEndTV.text =
                    "Akhir pengunaan: ${formatter.format(record.time_end?.toDate())}"
                Glide.with(root.context)
                    .load(R.drawable.logo_pdam)
                    .apply(RequestOptions().override(85, 85))
                    .into(logoIV)
            }
        }
    }
}