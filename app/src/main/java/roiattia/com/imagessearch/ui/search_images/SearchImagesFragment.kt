package roiattia.com.imagessearch.ui.search_images

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_search_images.*
import roiattia.com.imagessearch.R
import roiattia.com.imagessearch.data.domain_model.Image
import roiattia.com.imagessearch.databinding.FragmentSearchImagesBinding

@AndroidEntryPoint
class SearchImagesFragment : Fragment() {

    private val viewModel: SearchImagesViewModel by viewModels()

    lateinit var adapter: ImagesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(viewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return DataBindingUtil.inflate<FragmentSearchImagesBinding>(
            inflater, R.layout.fragment_search_images, container, false
        ).also {
            it.viewModel = this@SearchImagesFragment.viewModel
            it.lifecycleOwner = this
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        subscribeViewModelCommand()
    }

    private fun initRecyclerView() {
        adapter = ImagesAdapter(emptyList())
        rvImages.adapter = adapter
        rvImages.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val isBottomReached = !recyclerView.canScrollVertically(1)
                if (isBottomReached) {
                    viewModel.loadNextPage()
                }
            }
        })
    }

    private fun subscribeViewModelCommand() {
        viewModel.command.observe(viewLifecycleOwner, {
            when (it) {
                is SearchImagesViewModel.Command.UpdateImages -> updateImages(it.images)
            }
        })
    }

    private fun updateImages(images: List<Image>) {
        adapter.setData(images)
    }

}