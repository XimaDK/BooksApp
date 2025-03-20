package kadyshev.dmitry.data.repositories

import android.content.SharedPreferences
import com.google.gson.Gson
import kadyshev.dmitry.domain.entities.Book
import kadyshev.dmitry.domain.repositories.DataRepository

class DataRepositoryImpl(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) : DataRepository {

    override fun saveBookToDataSource(book: Book) {
        val books = getBooksFromDataSource().toMutableList()
        if (books.none { it.title == book.title }) {
            books.add(book)
            saveBooksList(books)
        }
    }

    override fun getBooksFromDataSource(): List<Book> {
        val booksJson = sharedPreferences.getString(BOOKS_KEY, null)
        return if (booksJson.isNullOrEmpty()) {
            emptyList()
        } else {
            try {
                gson.fromJson(booksJson, Array<Book>::class.java)?.toList() ?: emptyList()
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    override fun removeBooksFromDataSource(book: Book) {
        val books = getBooksFromDataSource().toMutableList()
        books.removeAll { it.title == book.title }
        saveBooksList(books)
    }

    private fun saveBooksList(books: List<Book>) {
        val booksJson = gson.toJson(books)
        sharedPreferences.edit().putString(BOOKS_KEY, booksJson).apply()
    }

    companion object {
        private const val BOOKS_KEY = "BOOKS_KEY"
    }
}
