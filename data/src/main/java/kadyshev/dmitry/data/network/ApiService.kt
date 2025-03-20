import kadyshev.dmitry.data.network.dto.BooksResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("volumes")
    suspend fun searchBooks(
        @Query("q") query: String
    ): Response<BooksResponseDto>
}
