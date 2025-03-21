package kadyshev.dmitry.booksapp.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kadyshev.dmitry.booksapp.utils.BookUtils
import kadyshev.dmitry.domain.entities.Book
import kadyshev.dmitry.domain.useCases.RemoveBooksFromDataSourceUseCase
import kadyshev.dmitry.domain.useCases.SaveBookToDataSourceUseCase


class DetailViewModel(
    private val saveBookToDataSourceUseCase: SaveBookToDataSourceUseCase,
    private val removeBooksFromDataSourceUseCase: RemoveBooksFromDataSourceUseCase
) : ViewModel() {


    fun onLikeBook(book: Book, onError: (String) -> Unit, onSuccess: (String, Boolean) -> Unit) {
        BookUtils.toggleBookLike(
            book,
            saveBookToDataSourceUseCase,
            removeBooksFromDataSourceUseCase,
            viewModelScope,
            {
                val updatedBook = book.copy(isLiked = !book.isLiked)
                if (updatedBook.isLiked) {
                    saveBookToDataSourceUseCase(updatedBook)
                } else {
                    removeBooksFromDataSourceUseCase(updatedBook)
                }
                onSuccess(BOOK_UPDATE, updatedBook.isLiked)
            },
            onError = { errorMessage ->
                onError(errorMessage)
            },
            onSuccess = { successMessage ->
                onSuccess(successMessage, book.isLiked)
            }
        )
    }
    companion object{
        private const val BOOK_UPDATE = "Книга успешно обновлена"
    }

}