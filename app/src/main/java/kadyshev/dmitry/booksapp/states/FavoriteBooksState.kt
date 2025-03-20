package kadyshev.dmitry.booksapp.states

import kadyshev.dmitry.domain.entities.Book

sealed class FavoriteBooksState {
    data object Idle : FavoriteBooksState()
    data class Success(val books: List<Book>) : FavoriteBooksState()
    data class Error(val message: String) : FavoriteBooksState()
}
