package com.example.myapp.fragments

import android.app.Dialog
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myapp.R
import com.example.myapp.databinding.FragmentTimerBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.MaterialTimePicker.INPUT_MODE_KEYBOARD
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
    private var time: Long? = null

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

        binding.untilFinish.setOnClickListener {
            showTimeDialog()
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
            if (time == null) {
                startCountDownTimer(60)

            } else {
                startCountDownTimer(time!!)

            }

        } else {
            binding.start.text = "Start"
            binding.untilFinish.text = "Set Time"
            binding.progressBar.progress = 0
            countDownTimer?.cancel()
            countDownTimer = null
            time = null
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
        val second = getSecond * SECOND
        coroutineScope.launch {
            countDownTimer = object : CountDownTimer(((second)), (1 * SECOND)) {
                override fun onTick(millisUntilFinished: Long) {
                    val secondsUntilFinished = millisUntilFinished / SECOND
                    if (isPaused) {
                        resumeSecond = secondsUntilFinished
                        cancel()
                    }
                    binding.untilFinish.text = selectedTimeConverter((secondsUntilFinished).toString())
                    if(time == null){
                        binding.progressBar.max = 60

                        binding.progressBar.setProgress(
                            (60 - (millisUntilFinished / SECOND)).toInt(), true)
                    }else {
                        binding.progressBar.max = time!!.toInt()
                        binding.progressBar.setProgress(
                            (time!! - (millisUntilFinished / SECOND)).toInt(), true)
                    }

                }

                override fun onFinish() {
                    Toast.makeText(requireContext(), "Timer End", Toast.LENGTH_SHORT).show()
                    playEndSound()
                }

            }.start()
        }

    }


    private fun showTimeDialog() {
        val dialog = Dialog(requireContext())
        dialog.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(true)
            setContentView(R.layout.select_time_dialog)

            val button = findViewById<Button>(R.id.select_button)
            val time = findViewById<EditText>(R.id.time_select)
            button.setOnClickListener {
                selectedTimeConverter(time.text.toString())
                this@TimerFragment.time = time.text.toString().toLong()
                dialog.dismiss()
            }
        }
        dialog.show()
    }


    private fun selectedTimeConverter(time: String) : String {
        val resultString: String
        val timeInt = time.toInt()
        val hours = timeInt / 3600
        val hourString: String
        val minutes = timeInt / 60 - hours * 60
        val minutesString: String
        val seconds = timeInt - (minutes * 60) - (hours * 3600)
        val secondString: String

        if (hours < 10) {
            hourString = "0$hours"
        } else {
            hourString = hours.toString()
        }
        if (minutes < 10) {
            minutesString = "0$minutes"
        } else {
            minutesString = minutes.toString()
        }

        if (seconds < 10) {
            secondString = "0$seconds"
        } else {
            secondString = seconds.toString()
        }

        resultString = "$hourString:$minutesString:$secondString"
        binding.untilFinish.text = resultString
        return resultString
    }


}