package kadyshev.dmitry.domain.repositories

import kadyshev.dmitry.domain.entities.Book

interface RemoteRepository {

    suspend fun fetchRemoteBooks(query: String): List<Book>

}