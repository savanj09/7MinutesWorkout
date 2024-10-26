package com.minutesworkout

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.minutesworkout.databinding.ActivityExerciseBinding

class ExerciseActivity : AppCompatActivity() {
    private var binding:ActivityExerciseBinding?=null
    private var restTimer:CountDownTimer? =null
    private var restProgress =0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarExercise)
        if (supportActionBar!=null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding?.toolbarExercise?.setNavigationOnClickListener {
            onBackPressed()
        }

        setUpRestView()

       // setContentView(R.layout.activity_exercise)

    }
    private fun setUpRestView(){
        if (restTimer!=null){
            restTimer?.cancel()
            restProgress=0
        }
       setRestProgressBar()
    }

    private fun setRestProgressBar() {
        binding?.progressBar?.progress = restProgress
        restTimer = object : CountDownTimer(10000, 1000) {
            override fun onTick(p0: Long) {
                restProgress++
                binding?.progressBar?.progress =10-restProgress
                binding?.tvTimer?.text = (10-restProgress).toString()
            }

            override fun onFinish() {
               Toast.makeText(this@ExerciseActivity,
                   "Here now we will start the excerise",
                   Toast.LENGTH_LONG).show()
            }

        }.start()


    }

    override fun onDestroy() {
        super.onDestroy()

        if (restTimer!=null){
            restTimer?.cancel()
            restProgress=0
        }
        binding = null
    }

}