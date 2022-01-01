package com.oyelabs.marvel.universe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class SplashActivity : AppCompatActivity() {

    private lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            finish()
            startActivity(Intent(this, MainActivity::class.java))
        }, 400)
    }
}