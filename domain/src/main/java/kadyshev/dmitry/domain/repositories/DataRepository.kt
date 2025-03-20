package kadyshev.dmitry.domain.repositories

import kadyshev.dmitry.domain.entities.Book


interface DataRepository {

    fun saveBookToDataSource(book: Book)
    fun getBooksFromDataSource(): List<Book>
    fun removeBooksFromDataSource(book: Book)

}