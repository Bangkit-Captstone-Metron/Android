package com.example.vin.metron.history

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class HistorySectionPagerAdapter(activity: AppCompatActivity): FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return HistoryTabFragment.newInstance(position)
    }
}