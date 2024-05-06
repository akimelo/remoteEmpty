package com.example.remote_config_empty

import android.app.Application

import io.karte.android.KarteApp
import io.karte.android.variables.Variables
//import io.karte.android.variables.Variable

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        KarteApp.setup(this)
        Variables.fetch()
    }
}