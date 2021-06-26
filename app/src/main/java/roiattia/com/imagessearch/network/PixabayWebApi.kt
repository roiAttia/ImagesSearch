package roiattia.com.imagessearch.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import roiattia.com.imagessearch.core.Constants.Network.GET_IMAGES
import roiattia.com.imagessearch.data.web_dto.ImagesListResponse

interface PixabayWebApi {

    @GET(GET_IMAGES)
    suspend fun getImages(
        @Query("q") query: String,
        @Query("key") apiKey: String
    ): Response<ImagesListResponse>

}