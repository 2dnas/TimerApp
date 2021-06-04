package com.example.myapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.example.myapp.adapters.ViewPagerFragmentAdapter
import com.example.myapp.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewPagerFragmentAdapter: ViewPagerFragmentAdapter
    private lateinit var viewPager2: ViewPager2
    private lateinit var tabLayout : TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewPagerFragmentAdapter = ViewPagerFragmentAdapter(this)

        viewPager2 = binding.viewpager

        tabLayout = binding.tabLayout

        viewPager2.adapter = viewPagerFragmentAdapter

        TabLayoutMediator(tabLayout,viewPager2) { tab , position ->
            tab.apply {
                when(position){
                    0 -> {
                        text = "Timer"
                        icon = getDrawable(R.drawable.ic_baseline_timer)
                    }

                    1 -> {
                        text = "Stopwatch"
                        icon = getDrawable(R.drawable.ic_baseline_stopwatch)
                    }
                }

            }
        }.attach()

    }
}