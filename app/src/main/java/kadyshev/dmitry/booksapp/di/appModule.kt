package kadyshev.dmitry.booksapp.di

import kadyshev.dmitry.booksapp.viewModels.DetailViewModel
import kadyshev.dmitry.booksapp.viewModels.FavoriteViewModel
import kadyshev.dmitry.booksapp.viewModels.FilterViewModel
import kadyshev.dmitry.booksapp.viewModels.SearchViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel<SearchViewModel> {
        SearchViewModel(get(), get(), get(), get())
    }

    viewModel<FilterViewModel> {
        FilterViewModel()
    }

    viewModel<FavoriteViewModel> {
        FavoriteViewModel(get(), get(), get())
    }

    viewModel<DetailViewModel> {
        DetailViewModel(get(), get())
    }

}