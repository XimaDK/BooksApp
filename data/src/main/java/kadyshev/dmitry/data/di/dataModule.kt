package kadyshev.dmitry.data.di

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import kadyshev.dmitry.data.repositories.DataRepositoryImpl
import kadyshev.dmitry.domain.repositories.DataRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val dataModule = module {

    single<DataRepository> {
        DataRepositoryImpl(get(), get())
    }

    single { Gson() }

    single<SharedPreferences> {
        androidApplication().getSharedPreferences("MySharedPrefs", Context.MODE_PRIVATE)
    }
}