package kadyshev.dmitry.domain.entities


data class Book(
    val imageUrl: String,
    val title: String,
    val author: String,
    val publicationDate: String,
    val description: String,
    var isLiked: Boolean = false
)