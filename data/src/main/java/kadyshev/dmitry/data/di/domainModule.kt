package kadyshev.dmitry.data.di

import kadyshev.dmitry.domain.useCases.FetchBookUseCase
import kadyshev.dmitry.domain.useCases.GetBooksFromDataSourceUseCase
import kadyshev.dmitry.domain.useCases.RemoveBooksFromDataSourceUseCase
import kadyshev.dmitry.domain.useCases.SaveBookToDataSourceUseCase
import org.koin.dsl.module

val domainModule = module {

    factory<FetchBookUseCase> {
        FetchBookUseCase(get())
    }
    factory<SaveBookToDataSourceUseCase> {
        SaveBookToDataSourceUseCase(get())
    }

    factory<RemoveBooksFromDataSourceUseCase> {
        RemoveBooksFromDataSourceUseCase(get())
    }

    factory<GetBooksFromDataSourceUseCase> {
        GetBooksFromDataSourceUseCase(get())
    }

}