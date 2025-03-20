package kadyshev.dmitry.domain.useCases

import kadyshev.dmitry.domain.entities.Book
import kadyshev.dmitry.domain.repositories.DataRepository

class RemoveBooksFromDataSourceUseCase(private val dataRepository: DataRepository) {
    operator fun invoke(book: Book) = dataRepository.removeBooksFromDataSource(book)
}