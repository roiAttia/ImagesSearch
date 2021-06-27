package roiattia.com.imagessearch.data.domain_model

import roiattia.com.imagessearch.data.mapper.Mapper
import roiattia.com.imagessearch.data.web_dto.ImageResponse

data class Image(
    val previewURL: String,
    val largeImageUrl: String
) {

    companion object {
        val NetworkMapper by lazy {
            object : Mapper<ImageResponse, Image> {
                override fun map(source: ImageResponse): Image {
                    source.apply {
                        return Image(
                            previewURL, largeImageUrl
                        )
                    }
                }
            }
        }
    }

}
