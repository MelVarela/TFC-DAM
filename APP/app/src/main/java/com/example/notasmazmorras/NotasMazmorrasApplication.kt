package com.example.notasmazmorras

import android.app.Application
import com.example.notasmazmorras.data.AppContainer
import com.example.notasmazmorras.data.AppDataContainer

class NotasMazmorrasApplication : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }

}