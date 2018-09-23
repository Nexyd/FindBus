package com.dani.kotlin.findbus

import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.widget.Button
import java.net.URL

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