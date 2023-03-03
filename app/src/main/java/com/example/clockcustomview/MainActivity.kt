package com.example.clockcustomview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private val showButton: Button by lazy { findViewById(R.id.btClock) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showButton.setOnClickListener {
            Toast.makeText(this, "Didn't have time", Toast.LENGTH_SHORT).show()
        }
    }
}