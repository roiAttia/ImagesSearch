package roiattia.com.imagessearch.data.domain_model

data class Image(
    val previewURL: String,
    val largeImageUrl: String,
    val tags: String,
    val comments: Int,
    val favorites: Int,
    val likes: Int
)
