package kadyshev.dmitry.data.mapper

import android.util.Log
import kadyshev.dmitry.data.network.dto.BookItemDto
import kadyshev.dmitry.domain.entities.Book

class Mapper {

    fun mapDtoToBook(item: BookItemDto): Book {
        Log.d("Mapper", "Mapping item: ${item.volumeInfo}")
        return Book(
            title = item.volumeInfo.title ?: "Неизвестно",
            author = item.volumeInfo.authors?.joinToString(", ") ?: "Неизвестно",
            publicationDate = item.volumeInfo.publishedDate ?: "Не найдено",
            description = item.volumeInfo.description ?: "Нет описания",
            imageUrl = item.volumeInfo.imageLinks?.thumbnail ?: ""
        )
    }

}