package com.minutesworkout

import android.os.Bundle
import android.util.Log
import android.view.View

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.minutesworkout.databinding.ActivityHistoryBinding
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {
    private var binding:ActivityHistoryBinding?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarHistoryActivity)
        if (supportActionBar!=null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setTitle("History")
        }

        binding?.toolbarHistoryActivity?.setNavigationOnClickListener {
            onBackPressed()

        }

        val dao = (application as WorkOutApp).db.historyDao()
        getAllCompletedDates(dao)

    }

    private fun getAllCompletedDates(historyDao: HistoryDao){

        lifecycleScope.launch {

            historyDao.fetchAllDates().collect{ allCompletedDatesList ->

                if (allCompletedDatesList.isNotEmpty()){
                    binding?.tvHistory?.visibility = View.VISIBLE
                    binding?.rvHistory?.visibility =View.VISIBLE
                    binding?.tvNoDataAvailable?.visibility = View.GONE

                    val dates = ArrayList<String>()
                    for (date in allCompletedDatesList){
                        dates.add(date.date)
                    }

                    binding?.rvHistory?.layoutManager = LinearLayoutManager(this@HistoryActivity)
                    val historyAdapter = HistoryAdapter(ArrayList(dates))
                    binding?.rvHistory?.adapter = historyAdapter

                }
                else{
                    binding?.tvHistory?.visibility = View.GONE
                    binding?.rvHistory?.visibility =View.GONE
                    binding?.tvNoDataAvailable?.visibility = View.VISIBLE
                }
                for (i in allCompletedDatesList){

                    Log.e("Date : ", ""+i.date)
                }

            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()

        binding = null
    }
}