package kadyshev.dmitry.domain.useCases

import kadyshev.dmitry.domain.repositories.RemoteRepository

class FetchBookUseCase(private val remoteRepository: RemoteRepository) {
    suspend operator fun invoke(query: String) = remoteRepository.fetchRemoteBooks(query)
}