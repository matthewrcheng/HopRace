package com.matthewrcheng.hoprace

import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import kotlin.math.pow

class MainActivity : ComponentActivity() {

    private var eggCount = 0
    private var eggsPerClick = 1
    private var eggsPerSecond = 0
    private var upgradeCount = 0
    private val baseUpgradeCost = 10
    private var upgradeCost = calcCost(baseUpgradeCost, upgradeCount)
    private var newRabbitCount = 0
    private val baseNewRabbitCost = 100
    private var newRabbitCost = calcCost(baseNewRabbitCost, newRabbitCount)
    private var farmCount = 0
    private val baseFarmCost = 50
    private var farmCost = calcCost(baseFarmCost, farmCount)
    private lateinit var preferences: SharedPreferences
    private lateinit var handler : Handler
    private lateinit var rabbitImage : ImageView
    private lateinit var eggCounter : TextView
    private lateinit var upgradeButton : Button
    private lateinit var newRabbitButton : Button
    private lateinit var farmButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // set content view
        setContentView(R.layout.activity_main)

        // init shared prefs
        preferences = getSharedPreferences("RabbitClicker", MODE_PRIVATE)
        eggCount = preferences.getInt("eggCount", 0) // Default to 0 if no value exists
        eggsPerClick = preferences.getInt("eggsPerClick", 1)
        eggsPerSecond = preferences.getInt("eggsPerSecond", 0)
        upgradeCount = preferences.getInt("upgradeCount", 0)
        upgradeCost = calcCost(baseUpgradeCost, upgradeCount)
        newRabbitCount = preferences.getInt("newRabbitCount", 0)
        newRabbitCost = calcCost(baseNewRabbitCost, newRabbitCount)
        farmCount = preferences.getInt("farmCount", 0)
        farmCost = calcCost(baseFarmCost, farmCount)
        val lastExitTime = preferences.getLong("lastExitTime", -1L)
        if (lastExitTime != -1L) {
            val elapsedSeconds = ((System.currentTimeMillis() - lastExitTime) / 1000).toInt()
            val missedEggs = elapsedSeconds * eggsPerSecond
            eggCount += missedEggs
        }

        // init views
        rabbitImage = findViewById(R.id.rabbitImage)
        eggCounter = findViewById(R.id.eggCounter)
        updateEggCounter()
        upgradeButton = findViewById(R.id.upgradeButton)
        upgradeButton.text = getString(R.string.upgrade, upgradeCost)
        newRabbitButton = findViewById(R.id.newRabbitButton)
        newRabbitButton.text = getString(R.string.new_rabbit, newRabbitCost)
        farmButton = findViewById(R.id.farmButton)
        farmButton.text = getString(R.string.farm, farmCost)

        handler = Handler(Looper.getMainLooper())

        rabbitImage.setOnClickListener {
            eggCount += eggsPerClick
            updateEggCounter()
        }

        upgradeButton.setOnClickListener {
            if (eggCount >= upgradeCost) {
                eggCount -= upgradeCost
                eggsPerClick += 1
                upgradeCount += 1
                upgradeCost = calcCost(baseUpgradeCost, upgradeCount)
                updateEggCounter()
                upgradeButton.text = getString(R.string.upgrade, upgradeCost)
            }
        }

        newRabbitButton.setOnClickListener {
            if (eggCount >= newRabbitCost) {
                eggCount -= newRabbitCost
                eggsPerClick += 10
                newRabbitCount += 1
                newRabbitCost = calcCost(baseNewRabbitCost, newRabbitCount)
                updateEggCounter()
                newRabbitButton.text = getString(R.string.new_rabbit, newRabbitCost)
            }
        }

        farmButton.setOnClickListener {
            if (eggCount >= farmCost) {
                eggCount -= farmCost
                eggsPerSecond += 1
                farmCount += 1
                farmCost = calcCost(baseFarmCost, farmCount)
                updateEggCounter()
                farmButton.text = getString(R.string.farm, farmCost)
            }
        }

        handler.post(autoGenerateTask)
    }

    private val autoGenerateTask = object : Runnable {
        override fun run() {
            eggCount += eggsPerSecond
            updateEggCounter()
            handler.postDelayed(this, 1000) // Run every second
        }
    }

    override fun onStart() {
        super.onStart()
//        with (preferences.edit()) {
//            putInt("eggCount", eggCount)
//            putInt("eggsPerClick", eggsPerClick)
//            putInt("eggsPerSecond", eggsPerSecond)
//            putInt("upgradeCount", upgradeCount)
//            putInt("newRabbitCount", newRabbitCount)
//            putInt("farmCount", farmCount)
//            apply()
//        }
        handler.post(autoGenerateTask)
    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacks(autoGenerateTask)
    }

    override fun onPause() {
        super.onPause()
        val editor = preferences.edit()
        editor.putInt("eggCount", eggCount)
        editor.putInt("eggsPerClick", eggsPerClick)
        editor.putInt("eggsPerSecond", eggsPerSecond)
        editor.putInt("upgradeCount", upgradeCount)
        editor.putInt("newRabbitCount", newRabbitCount)
        editor.putInt("farmCount", farmCount)
        editor.putLong("lastExitTime", System.currentTimeMillis())
        editor.apply()
    }

    override fun onResume() {
        super.onResume()
        eggCount = preferences.getInt("eggCount", 0)
        eggsPerClick = preferences.getInt("eggsPerClick", 1)
        eggsPerSecond = preferences.getInt("eggsPerSecond", 0)
        upgradeCount = preferences.getInt("upgradeCount", 0)
        upgradeCost = calcCost(baseUpgradeCost, upgradeCount)
        newRabbitCount = preferences.getInt("newRabbitCount", 0)
        newRabbitCost = calcCost(baseNewRabbitCost, newRabbitCount)
        farmCount = preferences.getInt("farmCount", 0)
        farmCost = calcCost(baseFarmCost, farmCount)
        val lastExitTime = preferences.getLong("lastExitTime", -1L)
        if (lastExitTime != -1L) {
            val elapsedSeconds = ((System.currentTimeMillis() - lastExitTime) / 1000).toInt()
            val missedEggs = elapsedSeconds * eggsPerSecond
            eggCount += missedEggs
        }
    }

    private fun calcCost(base: Int, count: Int): Int {
        val rate = 1.2
        return (base * rate.pow(count)).toInt()
    }

    private fun updateEggCounter() {
        eggCounter.text = getString(R.string.egg_count, eggCount)
    }
}

