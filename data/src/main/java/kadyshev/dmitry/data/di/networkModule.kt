package kadyshev.dmitry.data.di

import ApiService
import kadyshev.dmitry.data.mapper.Mapper
import kadyshev.dmitry.data.repositories.RemoteRepositoryImpl
import kadyshev.dmitry.domain.repositories.RemoteRepository
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/books/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single { Mapper() }

    single<RemoteRepository> {
        RemoteRepositoryImpl(get(), get())
    }

    single<ApiService> { get<Retrofit>().create(ApiService::class.java) }
}