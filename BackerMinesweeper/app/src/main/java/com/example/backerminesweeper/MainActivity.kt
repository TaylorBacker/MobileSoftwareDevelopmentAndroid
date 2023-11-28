package com.example.backerminesweeper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.WindowCompat
import com.example.backerminesweeper.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.resetButton.setOnClickListener {
            binding.mineSweeper.reset()
        }

        binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
            Log.d("checkboxes", "checked: $isChecked")
        }
    }

    fun setMessage(message: String) {
        binding.tvMessage.text = message
    }

    fun isFlagModeOn(): Boolean {
        return binding.checkbox.isChecked
    }
}