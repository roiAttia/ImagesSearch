package roiattia.com.imagessearch.data.repositories

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import roiattia.com.imagessearch.core.Constants
import roiattia.com.imagessearch.core.Constants.Network.API_KEY
import roiattia.com.imagessearch.data.web_dto.ImageResponse
import roiattia.com.imagessearch.network.PixabayWebApi
import javax.inject.Inject

interface ImagesRepository {

    val searchProgress: StateFlow<SearchProgress>

    suspend fun searchImages(query: String)

}

sealed class SearchProgress {
    object NotStarted : SearchProgress()
    object InProgress : SearchProgress()
    data class Success(val images: List<ImageResponse>) : SearchProgress()
    data class Failed(val errorMessage: String) : SearchProgress()
}

class ImageRepositoryImpl @Inject constructor(
    private val webApi: PixabayWebApi
) : ImagesRepository {

    private val _searchProgress = MutableStateFlow<SearchProgress>(SearchProgress.NotStarted)
    override val searchProgress: StateFlow<SearchProgress>
        get() = _searchProgress

    override suspend fun searchImages(query: String) {
        _searchProgress.value = SearchProgress.InProgress
        try {
            val response = webApi.getImages(query, API_KEY)
            if (response.isSuccessful) {
                response.body()?.let {
                    _searchProgress.value = SearchProgress.Success(it.hits)
                }
            }
        } catch (e: Exception) {
            _searchProgress.value = SearchProgress.Failed("Exception connecting to server ${e.message}")
        }
    }

}