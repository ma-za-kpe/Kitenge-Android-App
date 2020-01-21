package com.maku.kitenge

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import timber.log.Timber

class KitengeApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}
