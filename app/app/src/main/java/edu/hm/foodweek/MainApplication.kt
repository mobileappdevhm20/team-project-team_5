package edu.hm.foodweek

import android.app.Application
import edu.hm.foodweek.inject.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Init Koin
        startKoin {
            // use AndroidLogger as Koin Logger - default Level.INFO
            androidLogger()

            // use the Android context given there
            androidContext(this@MainApplication)

            // load properties from assets/koin.properties file
            //androidFileProperties()

            // module list
            modules(listOf(appModule))
        }
    }
}