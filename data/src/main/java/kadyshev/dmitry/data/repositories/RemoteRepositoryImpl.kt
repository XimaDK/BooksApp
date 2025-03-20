package kadyshev.dmitry.data.repositories

import ApiService
import android.util.Log
import kadyshev.dmitry.data.mapper.Mapper
import kadyshev.dmitry.domain.entities.Book
import kadyshev.dmitry.domain.repositories.RemoteRepository

class RemoteRepositoryImpl(
    private val apiService: ApiService,
    private val mapper: Mapper
) : RemoteRepository {

    override suspend fun fetchRemoteBooks(query: String): List<Book> {
        val response = apiService.searchBooks(query)
        Log.d("RemoteRepositoryImpl", response.toString())
        if (response.isSuccessful) {
            Log.d("RemoteRepositoryImpl", response.body().toString())
            return response.body()?.items?.map { item ->
                mapper.mapDtoToBook(item)
            } ?: emptyList()
        } else {
            throw Exception("Ошибка при запросе книг")
        }
    }

}