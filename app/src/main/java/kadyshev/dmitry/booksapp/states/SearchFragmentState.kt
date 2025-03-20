package kadyshev.dmitry.booksapp.states

import kadyshev.dmitry.domain.entities.Book

sealed class SearchFragmentState {

    data object Idle : SearchFragmentState()
    data object Loading : SearchFragmentState()
    data class Success(val books: List<Book>) : SearchFragmentState()
    data object Empty : SearchFragmentState()
    data object Error : SearchFragmentState()
}