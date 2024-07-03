package com.example.project1

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.project1.databinding.ActivityMainMenuBinding

class MainMenu : AppCompatActivity() {

    private lateinit var binding: ActivityMainMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize buttons
        val button1 = binding.button1
        val button2 = binding.button2
        val button3 = binding.button3

        // Set click listeners for each button
        button1.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("fragment", "frag1")
            startActivity(intent)
        }

        button2.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("fragment", "frag2")
            startActivity(intent)
        }

        button3.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("fragment", "frag3")
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        // Start MainActivity and navigate to Frag3
        finishAffinity()
    }
}
