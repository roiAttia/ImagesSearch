package roiattia.com.imagessearch.ui.search_images

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import roiattia.com.imagessearch.core.Constants.SharedPreferences.SEARCH_IMAGES_QUERY
import roiattia.com.imagessearch.core.PreferencesManager
import roiattia.com.imagessearch.data.domain_model.Image
import roiattia.com.imagessearch.data.domain_model.Image.Companion.NetworkMapper
import roiattia.com.imagessearch.data.mapper.ListMapperImpl
import roiattia.com.imagessearch.data.repositories.ImagesRepository
import roiattia.com.imagessearch.data.repositories.SearchState
import javax.inject.Inject

@HiltViewModel
class SearchImagesViewModel @Inject constructor(
    private val repository: ImagesRepository,
    private val prefsManager: PreferencesManager
) : ViewModel(), LifecycleObserver {

    private val _command = MutableLiveData<Command>()
    val command: LiveData<Command> = _command

    sealed class Command {
        data class UpdateImages(val images: List<Image>) : Command()
        data class UpdateProgressBar(val visible: Boolean) : Command()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private fun initView() {
        observeSearchState()
        searchImages(prefsManager.getString(SEARCH_IMAGES_QUERY, "") ?: "")
    }

    private fun observeSearchState() {
        viewModelScope.launch {
            repository.searchState.collect { searchProgress ->
                when (searchProgress) {
                    is SearchState.Success -> {
                        val images = ListMapperImpl(NetworkMapper).map(searchProgress.images)
                        _command.value = Command.UpdateImages(images)
                        _command.value = Command.UpdateProgressBar(false)
                    }
                    is SearchState.Failed -> {
                        _command.value = Command.UpdateProgressBar(false)
                    }
                    is SearchState.InState -> {
                        _command.value = Command.UpdateProgressBar(true)
                    }
                    is SearchState.NotStarted -> {
                    }
                }
            }
        }
    }

    fun searchImages(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.searchImages(query, 1)
        }
        prefsManager.putString(SEARCH_IMAGES_QUERY, query)
    }
}