package com.example.myapp.fragments

import android.graphics.Color
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import com.example.myapp.R
import com.example.myapp.databinding.FragmentStopwatchBinding
import kotlinx.coroutines.*
import java.util.*
import kotlin.math.min

class StopwatchFragment : Fragment(R.layout.fragment_stopwatch) {
    private lateinit var binding: FragmentStopwatchBinding
    private var isRunning = false
    private val coroutineScope = CoroutineScope(Dispatchers.Main.immediate)
    var pastTimeMillis : Long = 0
    var clicked = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStopwatchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.start.setOnClickListener {
            if(clicked) {
                coroutineScope.coroutineContext.cancelChildren()
                clicked = false
            }else {
                clicked = true
                isRunning = true
                pastTimeMillis = System.currentTimeMillis()
                binding.start.apply {
                    text = "Stop"
                    setTextColor(Color.RED)
                }
                coroutineScope.launch {
                    while (isRunning){
                        binding.stopwatch.text =  stopwatchEngine()
                    }
                }
            }


        }

    }


    private suspend fun stopwatchEngine() : String {

        return withContext(Dispatchers.Default) {
            var time = ""
            time = timeConverter(System.currentTimeMillis() - pastTimeMillis)
            time
        }

    }


    private fun timeConverter(time: Long): String {
        var milliseconds = time
        var seconds = milliseconds / 1000
        var minutes = milliseconds / 60000
        var hours = milliseconds / 3600000

        if(milliseconds > 1000L){
            milliseconds %= 1000
        }

        if (seconds == 60L) {
            seconds = 0
        }
        if (minutes == 60L) {
            minutes = 0
        }

        return "$hours:$minutes:$seconds:$milliseconds"
    }

}