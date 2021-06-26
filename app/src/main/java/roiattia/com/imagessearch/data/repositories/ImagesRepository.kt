package roiattia.com.imagessearch.data.repositories

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import roiattia.com.imagessearch.core.Constants
import roiattia.com.imagessearch.data.web_dto.ImageResponse
import roiattia.com.imagessearch.network.PixabayWebApi
import timber.log.Timber
import javax.inject.Inject

interface ImagesRepository {

    suspend fun fetchImages()

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

    private val _uiState = MutableStateFlow<SearchProgress>(SearchProgress.NotStarted)
    val uiState: StateFlow<SearchProgress> = _uiState

    override suspend fun fetchImages() {
        _uiState.value = SearchProgress.InProgress
        try {
            val response = webApi.getImages(Constants.Network.API_KEY)
            if (response.isSuccessful) {
                response.body()?.let {
                    _uiState.value = SearchProgress.Success(it.hits)
                }
            }
        } catch (e: Exception) {
            _uiState.value = SearchProgress.Failed("Exception connecting to server ${e.message}")
        }
    }

}