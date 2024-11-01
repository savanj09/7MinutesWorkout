package com.minutesworkout

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.Visibility
import com.minutesworkout.databinding.ActivityExerciseBinding
import java.util.Locale

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var binding:ActivityExerciseBinding?=null

    private var restTimer:CountDownTimer? =null
    private var restProgress =0

    private var exerciseTimer:CountDownTimer? =null
    private var exerciseProgress =0

    private var exerciseList:ArrayList<ExerciseModel>? =null
    private var currentExercisePosition = -1;

    private var tts: TextToSpeech? = null

    private var player:MediaPlayer? =null

    private var exerciseAdapter: ExerciseStatusAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarExercise)
        if (supportActionBar!=null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        exerciseList =Constants.defaultExerciseList()
        tts = TextToSpeech(this,this)
        binding?.toolbarExercise?.setNavigationOnClickListener {
            onBackPressed()
        }

        setUpRestView()
        setupExerciseStatusRecyclerView()

       // setContentView(R.layout.activity_exercise)

    }

    private fun setupExerciseStatusRecyclerView(){
        binding?.rvExcerciseStatus?.layoutManager =
            LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
           exerciseAdapter =ExerciseStatusAdapter(exerciseList!!)
        binding?.rvExcerciseStatus?.adapter = exerciseAdapter
    }
    private fun setUpRestView(){
        try {
          val soundURI = Uri.parse("android.resource://com.minutesworkout/" +R.raw.press_start)
          player =MediaPlayer.create(applicationContext,soundURI)
          player?.isLooping = false
          player?.start()

        }
        catch (e:Exception){
            e.printStackTrace()
        }
        binding?.flRestView?.visibility =View.VISIBLE
        binding?.tvTitle?.visibility =View.VISIBLE
        binding?.upcomingLabel?.visibility = View.VISIBLE
        binding?.tvUpcomingExerciseName?.visibility = View.VISIBLE
        binding?.tvExerciseName?.visibility =View.INVISIBLE
        binding?.flExerciseView?.visibility =View.INVISIBLE
        binding?.ivImage?.visibility = View.INVISIBLE
        if (restTimer!=null){
            restTimer?.cancel()
            restProgress=0
        }
        binding?.tvUpcomingExerciseName?.text = exerciseList!![currentExercisePosition + 1].getName()
       setRestProgressBar()
    }

    private fun setUpExerciseView(){
        binding?.flRestView?.visibility =View.INVISIBLE
        binding?.tvTitle?.visibility =View.INVISIBLE
        binding?.tvUpcomingExerciseName?.visibility = View.INVISIBLE
        binding?.upcomingLabel?.visibility = View.INVISIBLE
        binding?.tvExerciseName?.visibility =View.VISIBLE
        binding?.flExerciseView?.visibility =View.VISIBLE
        binding?.ivImage?.visibility = View.VISIBLE


        if (exerciseTimer!=null){
            exerciseTimer?.cancel()
            exerciseProgress=0
        }
        speakOut(exerciseList!![currentExercisePosition].getName())
        binding?.ivImage?.setImageResource(exerciseList!![currentExercisePosition].getImage())
        binding?.tvExerciseName?.text = exerciseList!!.get(currentExercisePosition).getName()

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
                currentExercisePosition++

                exerciseList!![currentExercisePosition].setIsSelected(true)
                exerciseAdapter!!.notifyDataSetChanged()
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
                exerciseList!![currentExercisePosition].setIsSelected(false)
                exerciseList!![currentExercisePosition].setIsCompleted(true)
                exerciseAdapter!!.notifyDataSetChanged()
                if (currentExercisePosition<exerciseList?.size!!-1){
                    setUpRestView()
                }
                else{

                    Toast.makeText(
                        this@ExerciseActivity,
                        "Congratulations! You have completed the 7 minutes workout.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

        }.start()


    }

    override fun onDestroy() {


        if (restTimer!=null){
            restTimer?.cancel()
            restProgress=0
        }

        if (exerciseTimer!=null){
            exerciseTimer?.cancel()
            exerciseProgress=0
        }

        if (tts!= null) {
            tts!!.stop()
            tts!!.shutdown()

        }

        if(player!=null){
            player!!.stop()
        }
        super.onDestroy()
        binding = null
    }

    override fun onInit(status: Int) {
      if(status == TextToSpeech.SUCCESS){
          val  result = tts?.setLanguage(Locale.US)
          if (result ==TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
              Log.e("TTS", "The Language specified not supported")
          }

      }
        else{
            Log.e("TTS","Initialization Failed")
        }
    }

    private fun speakOut(text :String){
        tts!!.speak(text,TextToSpeech.QUEUE_FLUSH,null,"")
    }

}