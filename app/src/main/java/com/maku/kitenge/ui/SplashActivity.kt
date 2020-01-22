package com.maku.kitenge.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.databinding.DataBindingUtil
import com.maku.kitenge.R
import com.maku.kitenge.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    //glue between the layout and its views (binding variable)
    private lateinit var binding: ActivitySplashBinding

    private val SPLASH_TIME_OUT:Long=3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivitySplashBinding>(this, R.layout.activity_splash)

        Handler().postDelayed({

            startActivity(Intent(this, MainActivity::class.java))

            finish()

        }, SPLASH_TIME_OUT)    }
}
