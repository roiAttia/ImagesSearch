package roiattia.com.imagessearch.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import roiattia.com.imagessearch.core.Constants.Network.SEARCH_IMAGES
import roiattia.com.imagessearch.data.web_dto.ImagesListResponse

interface PixabayWebApi {

    @GET(SEARCH_IMAGES)
    suspend fun searchImages(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("key") apiKey: String
    ): Response<ImagesListResponse>

}