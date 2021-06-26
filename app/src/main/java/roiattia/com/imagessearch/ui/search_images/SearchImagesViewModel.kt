package roiattia.com.imagessearch.ui.search_images

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import roiattia.com.imagessearch.data.domain_model.Image
import roiattia.com.imagessearch.data.domain_model.Image.Companion.NetworkMapper
import roiattia.com.imagessearch.data.mapper.ListMapperImpl
import roiattia.com.imagessearch.data.repositories.ImagesRepository
import roiattia.com.imagessearch.data.repositories.SearchProgress
import javax.inject.Inject

@HiltViewModel
class SearchImagesViewModel @Inject constructor(
    private val repository: ImagesRepository
) : ViewModel(), LifecycleObserver {

    private val _imagesList = MutableLiveData<List<Image>>()
    val imagesList: LiveData<List<Image>> = _imagesList

    private val _command = MutableLiveData<Command>()
    val command: LiveData<Command> = _command

    sealed class Command {
        data class UpdateImages(val images: List<Image>): Command()
        data class UpdateProgressBar(val visible: Boolean): Command()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun initView() {
        viewModelScope.launch {
            loadSearchProgress()
        }
        fetchImages("kittens")
    }

    private suspend fun loadSearchProgress() {
        repository.searchProgress.collect { searchProgress ->
            when (searchProgress) {
                is SearchProgress.Success -> {
                    val images = ListMapperImpl(NetworkMapper).map(searchProgress.images)
                    _command.value = Command.UpdateImages(images)
                    _command.value = Command.UpdateProgressBar(false)
                }
                is SearchProgress.Failed -> {
                    _command.value = Command.UpdateProgressBar(false)
                }
                is SearchProgress.InProgress -> {
                    _command.value = Command.UpdateProgressBar(true)
                }
                is SearchProgress.NotStarted -> {
                }
            }
        }
    }

    fun fetchImages(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.searchImages(query)
        }
    }

}