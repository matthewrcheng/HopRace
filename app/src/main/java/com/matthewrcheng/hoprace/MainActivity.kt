package com.matthewrcheng.hoprace

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.matthewrcheng.hoprace.ui.theme.HopRaceTheme
import androidx.appcompat.app.AppCompatActivity

class MainActivity : ComponentActivity() {

    private var eggCount = 0
    private var eggsPerClick = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rabbitImage = findViewById<ImageView>(R.id.rabbitImage)
        val eggCounter = findViewById<TextView>(R.id.eggCounter)
        val upgradeButton = findViewById<Button>(R.id.upgradeButton)
        val newRabbitButton = findViewById<Button>(R.id.newRabbitButton)

        rabbitImage.setOnClickListener {
            eggCount += eggsPerClick
            eggCounter.text = "Eggs: $eggCount"
        }

        upgradeButton.setOnClickListener {
            if (eggCount >= 10) {
                eggCount -= 10
                eggsPerClick += 1
                eggCounter.text = "Eggs: $eggCount"
            }
        }

        newRabbitButton.setOnClickListener {
            if (eggCount >= 100) {
                eggCount -= 100
                eggsPerClick += 10
                eggCounter.text = "Eggs: $eggCount"
            }
        }

    }
}