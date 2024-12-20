package com.minutesworkout

import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.minutesworkout.databinding.ActivityFinishBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class FinishActivity : AppCompatActivity() {
    private var binding: ActivityFinishBinding?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFinishBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarFinishActivity)
        if (supportActionBar!=null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        binding?.toolbarFinishActivity?.setNavigationOnClickListener {
            onBackPressed()
        }

        binding?.btnFinish?.setOnClickListener {
            finish()
        }

        val dao = (application as WorkOutApp).db.historyDao()

        addDatetoDatabase(dao)

    }

    private fun addDatetoDatabase(historyDao: HistoryDao){

        val c = Calendar.getInstance()
        val dateTime = c.time
        Log.e("Date",""+dateTime)

        val sdf = SimpleDateFormat("dd MMM yyyy HH:mm:ss",Locale.getDefault())
        val date = sdf.format(dateTime)

        Log.e("Formated Date : ",""+date)
        lifecycleScope.launch {
            historyDao.insert(HistoryEntity(date))

            Log.e("Date : ","Added...")


        }


    }
}