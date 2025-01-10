package com.matthewrcheng.hoprace

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {

    private val preferences = getPreferences(Context.MODE_PRIVATE)
    private var eggCount = preferences.getInt("egg_count", 0) // Default to 0 if no value exists
    private var eggsPerClick = 1
    private val handler = Handler(Looper.getMainLooper())
    private var eggsPerSecond = 0
    private val rabbitImage = findViewById<ImageView>(R.id.rabbitImage)
    private val eggCounter = findViewById<TextView>(R.id.eggCounter)
    private val upgradeButton = findViewById<Button>(R.id.upgradeButton)
    private val newRabbitButton = findViewById<Button>(R.id.newRabbitButton)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        eggCounter.text = getString(R.string.egg_count, eggCount)

        rabbitImage.setOnClickListener {
            eggCount += eggsPerClick
            eggCounter.text = getString(R.string.egg_count, eggCount)
        }

        upgradeButton.setOnClickListener {
            if (eggCount >= 10) {
                eggCount -= 10
                eggsPerClick += 1
                eggCounter.text = getString(R.string.egg_count, eggCount)
                upgradeButton.text = getString(R.string.upgrade, 10)
            }
        }

        newRabbitButton.setOnClickListener {
            if (eggCount >= 100) {
                eggCount -= 100
                eggsPerClick += 10
                eggCounter.text = getString(R.string.egg_count, eggCount)
                newRabbitButton.text = getString(R.string.new_rabbit, 100)
            }
        }
    }

    private val autoGenerateTask = object : Runnable {
        override fun run() {
            eggCount += eggsPerSecond
            eggCounter.text = "eggs: $eggCount"
            handler.postDelayed(this, 1000) // Run every second
        }
    }

    override fun onStart() {
        super.onStart()
        with (preferences.edit()) {
            putInt("egg_count", eggCount)
            apply()
        }
        handler.post(autoGenerateTask)
    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacks(autoGenerateTask)
    }

    override fun onPause() {
        super.onPause()
        val prefs = getSharedPreferences("RabbitClicker", MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putInt("eggCount", eggCount)
        editor.putInt("eggsPerClick", eggsPerClick)
        editor.putInt("eggsPerSecond", eggsPerSecond)
        editor.apply()
    }

    override fun onResume() {
        super.onResume()
        val prefs = getSharedPreferences("RabbitClicker", MODE_PRIVATE)
        eggCount = prefs.getInt("eggCount", 0)
        eggsPerClick = prefs.getInt("eggsPerClick", 1)
        eggsPerSecond = prefs.getInt("eggsPerSecond", 0)
        eggCounter.text = "eggs: $eggCount"
    }
}

