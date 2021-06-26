package roiattia.com.imagessearch.data.domain_model

import roiattia.com.imagessearch.data.mapper.Mapper
import roiattia.com.imagessearch.data.web_dto.ImageResponse

data class Image(
    val previewURL: String,
    val largeImageUrl: String,
    val tags: String,
    val comments: Int,
    val favorites: Int,
    val likes: Int
) {

    companion object {
        val NetworkMapper by lazy {
            object : Mapper<ImageResponse, Image> {
                override fun map(source: ImageResponse): Image {
                    source.apply {
                        return Image(
                            previewURL, largeImageUrl, tags, comments, favorites, likes
                        )
                    }
                }
            }
        }
    }

}
