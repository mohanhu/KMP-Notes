package org.codeloop.notes.core

import android.app.Application
import org.codeloop.notes.features.notes.di.initKoin
import org.koin.android.ext.koin.androidContext

class CoreApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidContext(this@CoreApplication)
        }

    }

}