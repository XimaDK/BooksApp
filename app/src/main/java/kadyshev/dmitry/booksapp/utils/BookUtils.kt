package kadyshev.dmitry.booksapp.utils

import android.util.Log
import kadyshev.dmitry.domain.entities.Book
import kadyshev.dmitry.domain.useCases.RemoveBooksFromDataSourceUseCase
import kadyshev.dmitry.domain.useCases.SaveBookToDataSourceUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

object BookUtils {

    private const val ERROR_ADD = "Ошибка при добавлении в избранное"
    private const val ERROR_REMOVE = "Ошибка при удалении из избранного"
    private const val ADDED = "Книга успешно добавлена в избранное"
    private const val REMOVED = "Книга успешно удалена из избранного"
    private const val UNKNOWN_ERROR = "Неизвестная ошибка"

    fun toggleBookLike(
        book: Book,
        saveBookToDataSourceUseCase: SaveBookToDataSourceUseCase,
        removeBooksFromDataSourceUseCase: RemoveBooksFromDataSourceUseCase,
        coroutineScope: CoroutineScope,
        updateState: (Book) -> Unit,
        onError: (String) -> Unit,
        onSuccess: (String) -> Unit
    ) {
        coroutineScope.launch {
            try {
                val updatedBook = book.copy(isLiked = !book.isLiked)

                if (updatedBook.isLiked) {
                    try {
                        saveBookToDataSourceUseCase(updatedBook)
                        onSuccess(ADDED)
                    } catch (e: Exception) {
                        onError(ERROR_ADD)
                        return@launch
                    }
                } else {
                    try {
                        removeBooksFromDataSourceUseCase(updatedBook)
                        onSuccess(REMOVED)
                    } catch (e: Exception) {
                        onError(ERROR_REMOVE)
                        return@launch
                    }
                }
                updateState(updatedBook)
            } catch (e: Exception) {
                onError(UNKNOWN_ERROR)
            }
        }
    }
}
