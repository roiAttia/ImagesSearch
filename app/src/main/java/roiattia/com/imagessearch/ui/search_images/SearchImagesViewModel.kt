package roiattia.com.imagessearch.ui.search_images

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import roiattia.com.imagessearch.core.Constants.SharedPreferences.SEARCH_IMAGES_QUERY
import roiattia.com.imagessearch.core.PreferencesManager
import roiattia.com.imagessearch.data.domain_model.Image
import roiattia.com.imagessearch.data.domain_model.Image.Companion.NetworkMapper
import roiattia.com.imagessearch.data.mapper.ListMapperImpl
import roiattia.com.imagessearch.data.repositories.ImagesRepository
import roiattia.com.imagessearch.data.repositories.SearchState
import roiattia.com.imagessearch.data.web_dto.ImageResponse
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SearchImagesViewModel @Inject constructor(
    private val repository: ImagesRepository,
    private val prefsManager: PreferencesManager
) : ViewModel(), LifecycleObserver {

    var query = prefsManager.getString(SEARCH_IMAGES_QUERY, "") ?: ""
    private var page = 1
    private var imagesList = ArrayList<Image>()

    private val _showProgressBar = MutableLiveData<Boolean>()
    val showProgressBar: LiveData<Boolean> = _showProgressBar

    private val _command = MutableLiveData<Command>()
    val command: LiveData<Command> = _command

    sealed class Command {
        data class UpdateImages(val images: List<Image>) : Command()
    }

    fun onGoClicked() {
        page = 1
        searchImages(query, page)
        prefsManager.putString(SEARCH_IMAGES_QUERY, query)
    }

    fun loadNextPage() {
        page += 1
        searchImages(query, page)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private fun initView() {
        observeSearchState()
        searchImages(query, page)
    }

    private fun observeSearchState() {
        viewModelScope.launch {
            repository.searchState.collect { searchProgress ->
                when (searchProgress) {
                    is SearchState.Success -> {
                        if(searchProgress.images.isNotEmpty()){
                            handleImages(searchProgress.images)
                        } else{
                            //TODO: handle no search results
                        }
                    }
                    is SearchState.Failed -> {
                        _showProgressBar.value = false
                        Timber.d(searchProgress.errorMessage)
                    }
                    is SearchState.InProgress -> {
                        _showProgressBar.value = true
                    }
                    is SearchState.NotStarted -> {
                    }
                }
            }
        }
    }

    private fun handleImages(responseImages: List<ImageResponse>) {
        val images = ListMapperImpl(NetworkMapper).map(responseImages)
        //TODO: handle case where loaded last page
        if (page > 1) {
            imagesList.addAll(images)
        } else {
            imagesList = images as ArrayList<Image>
        }
        _command.value = Command.UpdateImages(imagesList)
        _showProgressBar.value = false
    }

    private fun searchImages(query: String, page: Int){
        viewModelScope.launch {
            repository.searchImages(query, page)
        }
    }
}