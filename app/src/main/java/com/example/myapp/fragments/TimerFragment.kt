package com.example.myapp.fragments

import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myapp.R
import com.example.myapp.databinding.FragmentTimerBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


const val SECOND = 1000L

class TimerFragment : Fragment(R.layout.fragment_timer) {
    private lateinit var binding: FragmentTimerBinding
    private lateinit var player: MediaPlayer
    private var countDownTimer: CountDownTimer? = null
    private var isPaused = false
    private var resumeSecond = 0L
    private var coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main.immediate)
    private lateinit var time : String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTimerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        player = MediaPlayer.create(requireContext(), R.raw.timer)

        binding.untilFinish.setOnClickListener {

        }

        binding.start.setOnClickListener {
            coroutineScope.launch {
                startTimer()
            }
        }

        binding.pause.setOnClickListener {
            if (isPaused) {
                coroutineScope.launch {
                    continueTimer()
                }
            } else {
                pauseTimer()
                binding.pause.text = "Continue"
            }

        }
    }


    private suspend fun startTimer() {
        if (countDownTimer == null) {
            binding.start.text = "Stop"
            startCountDownTimer(60)

        } else {
            binding.start.text = "Start"
            binding.untilFinish.text = "Set Time"
            binding.progressBar.progress = 0
            countDownTimer?.cancel()
            countDownTimer = null
        }

    }


    private fun playEndSound() {
        player.start()
    }

    private fun pauseTimer() {
        isPaused = true
    }

    private suspend fun continueTimer() {
        isPaused = false
        startCountDownTimer(resumeSecond)
        binding.pause.text = "Pause"
    }

    private suspend fun startCountDownTimer(getSecond: Long) {
        var second = getSecond * SECOND
        coroutineScope.launch {
                countDownTimer = object : CountDownTimer(((second)), (1 * SECOND)) {
                    override fun onTick(millisUntilFinished: Long) {
                        if (isPaused) {
                            resumeSecond = millisUntilFinished / SECOND
                            cancel()
                        }
                        binding.untilFinish.text = (millisUntilFinished / SECOND).toString()
                        binding.progressBar.progress = (60 - (millisUntilFinished / SECOND)).toInt()


                    }

                    override fun onFinish() {
                        Toast.makeText(requireContext(), "Timer End", Toast.LENGTH_SHORT).show()
                        playEndSound()
                    }

                }.start()
            }

    }



    fun showTimeDialog(){

    }


}