package com.maku.kitenge

import android.app.Application
import android.content.Context
import timber.log.Timber


class KitengeApp : Application() {

    //context
    init {
        instance = this
    }

    companion object {
        private var instance: KitengeApp? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        //timber
        Timber.plant(Timber.DebugTree())

        //fonts

    }
}