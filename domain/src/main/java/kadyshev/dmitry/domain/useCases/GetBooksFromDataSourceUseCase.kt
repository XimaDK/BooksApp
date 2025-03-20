package kadyshev.dmitry.domain.useCases

import kadyshev.dmitry.domain.repositories.DataRepository


class GetBooksFromDataSourceUseCase(private val dataRepository: DataRepository) {
    operator fun invoke() = dataRepository.getBooksFromDataSource()
}