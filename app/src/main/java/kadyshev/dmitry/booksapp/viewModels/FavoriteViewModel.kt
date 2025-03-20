package kadyshev.dmitry.booksapp.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kadyshev.dmitry.booksapp.states.FavoriteBooksState
import kadyshev.dmitry.booksapp.utils.BookUtils
import kadyshev.dmitry.domain.entities.Book
import kadyshev.dmitry.domain.useCases.GetBooksFromDataSourceUseCase
import kadyshev.dmitry.domain.useCases.RemoveBooksFromDataSourceUseCase
import kadyshev.dmitry.domain.useCases.SaveBookToDataSourceUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FavoriteViewModel(
    private val getBooksFromDataSourceUseCase: GetBooksFromDataSourceUseCase,
    private val saveBookToDataSourceUseCase: SaveBookToDataSourceUseCase,
    private val removeBooksFromDataSourceUseCase: RemoveBooksFromDataSourceUseCase
) : ViewModel() {

    private val _favoriteBooksState = MutableStateFlow<FavoriteBooksState>(FavoriteBooksState.Idle)
    val favoriteBooksState: StateFlow<FavoriteBooksState> get() = _favoriteBooksState

    init {
        loadFavoriteBooks()
    }

    fun onLikeBook(book: Book, onError: (String) -> Unit, onSuccess: (String) -> Unit) {
        BookUtils.toggleBookLike(
            book,
            saveBookToDataSourceUseCase,
            removeBooksFromDataSourceUseCase,
            viewModelScope,
            { updatedBook ->
                val currentState = _favoriteBooksState.value
                if (currentState is FavoriteBooksState.Success) {
                    val updatedList = currentState.books.map {
                        if (it.title == updatedBook.title) updatedBook else it
                    }
                    _favoriteBooksState.value = FavoriteBooksState.Success(updatedList)
                }
            },
            onError = { errorMessage ->
                onError(errorMessage)
            },
            onSuccess = { successMessage ->
                onSuccess(successMessage)
            }
        )
    }

    private fun loadFavoriteBooks() {
        _favoriteBooksState.value = FavoriteBooksState.Idle
        try {
            val books = getBooksFromDataSourceUseCase.invoke()
            _favoriteBooksState.value =
                FavoriteBooksState.Success(books)
        } catch (e: Exception) {
            _favoriteBooksState.value =
                FavoriteBooksState.Error(e.message ?: UNKNOWN_ERROR)
        }
    }

    companion object{
        const val UNKNOWN_ERROR = "Неизвестная ошибка"
    }
}

