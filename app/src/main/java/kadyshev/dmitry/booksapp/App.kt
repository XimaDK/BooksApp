package kadyshev.dmitry.booksapp

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import kadyshev.dmitry.booksapp.di.appModule
import kadyshev.dmitry.data.di.dataModule
import kadyshev.dmitry.data.di.domainModule
import kadyshev.dmitry.data.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        startKoin {
            androidContext(this@App)
            modules(networkModule, dataModule, domainModule, appModule)
        }
    }
}