package com.dani.kotlin.findbus

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val showMap: Button = findViewById(R.id.btn_ShowMap)
        showMap.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            // intent.putExtra("key", value)
            startActivity(intent)
        }
    }
}