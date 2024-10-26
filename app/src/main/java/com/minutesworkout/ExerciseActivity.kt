package com.minutesworkout

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.transition.Visibility
import com.minutesworkout.databinding.ActivityExerciseBinding

class ExerciseActivity : AppCompatActivity() {
    private var binding:ActivityExerciseBinding?=null
    private var restTimer:CountDownTimer? =null
    private var restProgress =0
    private var exerciseTimer:CountDownTimer? =null
    private var exerciseProgress =0


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

    private fun setUpExerciseView(){
        binding?.flProgressbar?.visibility =View.INVISIBLE
        binding?.tvTitle?.text = "Exercise Name"
        binding?.flExerciseView?.visibility = View.VISIBLE
        if (exerciseTimer!=null){
            exerciseTimer?.cancel()
            exerciseProgress=0
        }
        setExerciseProgressBar()
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
                setUpExerciseView()
            }

        }.start()


    }
    private fun setExerciseProgressBar() {
        binding?.progressBarExercice?.progress = exerciseProgress
        exerciseTimer = object : CountDownTimer(30000, 1000) {
            override fun onTick(p0: Long) {
                exerciseProgress++
                binding?.progressBarExercice?.progress =30-exerciseProgress
                binding?.tvTimerExercise?.text = (30-exerciseProgress).toString()
            }

            override fun onFinish() {
                Toast.makeText(this@ExerciseActivity,
                    "30 seconds are over, lets go to the rest view",
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

        if (exerciseTimer!=null){
            exerciseTimer?.cancel()
            exerciseProgress=0
        }
        binding = null
    }

}