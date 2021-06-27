package roiattia.com.imagessearch.data.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import roiattia.com.imagessearch.core.Constants.Network.API_KEY
import roiattia.com.imagessearch.data.web_dto.ImageResponse
import roiattia.com.imagessearch.network.PixabayWebApi
import timber.log.Timber
import javax.inject.Inject

interface ImagesRepository {

    val searchState: StateFlow<SearchState>

    suspend fun searchImages(query: String, page: Int)
}

sealed class SearchState {
    object NotStarted : SearchState()
    object InProgress: SearchState()
    data class Success(val images: List<ImageResponse>) : SearchState()
    data class Failed(val errorMessage: String) : SearchState()
}

class ImageRepositoryImpl @Inject constructor(
    private val webApi: PixabayWebApi
) : ImagesRepository {

    private val _searchState = MutableStateFlow<SearchState>(SearchState.NotStarted)
    override val searchState: StateFlow<SearchState>
        get() = _searchState

    override suspend fun searchImages(query: String, page: Int) = withContext(Dispatchers.IO)  {
        Timber.d("searchImages query = $query page = $page")
        _searchState.value = SearchState.InProgress
        try {
            val response = webApi.searchImages(query, page, API_KEY)
            if (response.isSuccessful) {
                response.body()?.let {
                    _searchState.value = SearchState.Success(it.hits)
                }
            }
        } catch (e: Exception) {
            _searchState.value = SearchState.Failed("Exception ${e.message}")
        }
    }

}