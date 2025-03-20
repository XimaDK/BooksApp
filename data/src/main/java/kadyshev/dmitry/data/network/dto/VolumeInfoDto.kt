package kadyshev.dmitry.data.network.dto

data class VolumeInfoDto(
    val title: String?,
    val authors: List<String>?,
    val publishedDate: String?,
    val description: String?,
    val imageLinks: ImageLinksDto?
)