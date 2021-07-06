package kg.tutorialapp.wheather_final_project

import android.app.Application
import kg.tutorialapp.wheather_final_project.di.dataModule
import kg.tutorialapp.wheather_final_project.di.vmModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App: Application(){

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(listOf(vmModule, dataModule))
        }
    }
}