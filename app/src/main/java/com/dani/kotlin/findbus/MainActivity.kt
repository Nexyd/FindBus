package com.dani.kotlin.findbus

import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermission()
        val showMap: Button = findViewById(R.id.btn_ShowMap)
        showMap.setOnClickListener {
            val intent = Intent(this,
                MapsActivity::class.java)

            startActivity(intent)
        }
    }

    private fun checkPermission() {
        if (ActivityCompat.checkSelfPermission(this,
            android.Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                MapsActivity.LOCATION_PERMISSION_REQUEST_CODE
            )

            return
        }
    }
}