package com.minutesworkout

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.minutesworkout.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var  binding: ActivityMainBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        //setContentView(R.layout.activity_main)
        binding?.flstart?.setOnClickListener {
           val intent = Intent(this,ExerciseActivity::class.java)
            startActivity(intent)
            //Toast.makeText(this,"Here we will start Excersise", Toast.LENGTH_LONG).show()
        }
        binding?.flBMI?.setOnClickListener {
            val intent = Intent(this,BMIActivity::class.java)
            startActivity(intent)
        }
    }
    override fun onDestroy() {
        super.onDestroy()

        binding =null
    }
}