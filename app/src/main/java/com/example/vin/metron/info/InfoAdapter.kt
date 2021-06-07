package com.example.vin.metron.info

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vin.metron.R
import com.example.vin.metron.data.remote.dummy.News
import com.example.vin.metron.databinding.ItemCardNewsBinding

class ListNewsAdapter(private val listNews:ArrayList<News>,private val onClickCallback: AdapterCallback) : RecyclerView.Adapter<ListNewsAdapter.ListViewHolder>() {

    inner class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)  {
        private val binding = ItemCardNewsBinding.bind(itemView)
        fun bind(news: News){
            binding.tvTitle.text = news.title
            binding.tvDetail.text = news.detail
            binding.tvDate.text = news.date
            Glide.with(itemView.context).load(news.photo)
                .error(R.drawable.ic_launcher_background).into(binding.ivNewsImage)
            binding.btnReadMore.setOnClickListener{onClickCallback.onButtonClick(news)}
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_card_news, parent,false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int = listNews.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val news = listNews[position]
        holder.bind(news)
    }
}