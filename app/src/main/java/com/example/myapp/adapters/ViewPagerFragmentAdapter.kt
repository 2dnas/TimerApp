package com.example.myapp.adapters

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myapp.fragments.StopwatchFragment
import com.example.myapp.fragments.TimerFragment

const val NUM_PAGES = 2
class ViewPagerFragmentAdapter(activity : AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount() = NUM_PAGES

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> TimerFragment()
            1 -> StopwatchFragment()
            else -> TimerFragment()
        }
    }

}