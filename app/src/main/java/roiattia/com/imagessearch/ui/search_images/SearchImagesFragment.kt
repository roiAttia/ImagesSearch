package roiattia.com.imagessearch.ui.search_images

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_search_images.*
import roiattia.com.imagessearch.R
import roiattia.com.imagessearch.data.domain_model.Image

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
    ): View? {
        return inflater.inflate(R.layout.fragment_search_images, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        subscribeViewModelCommand()
        btnSearch.setOnClickListener {
            viewModel.searchImages(etSearchQuery.text.toString())
        }
    }

    private fun initRecyclerView() {
        adapter = ImagesAdapter(emptyList())
        rvImages.adapter = adapter
//        rvImages.addOnScrollListener(object  : RecyclerView.OnScrollListener() {
//
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                val layoutManager = rvImages.layoutManager
//                if(layoutManager != null && layoutManager.findLast)
//            }
//        })
    }

    private fun subscribeViewModelCommand() {
        viewModel.command.observe(viewLifecycleOwner, {
            when (it) {
                is SearchImagesViewModel.Command.UpdateImages -> updateImages(it.images)
                is SearchImagesViewModel.Command.UpdateProgressBar -> updateProgressBar(it.visible)
            }
        })
    }

    private fun updateImages(images: List<Image>) {
        adapter.setData(images)
    }

    private fun updateProgressBar(visible: Boolean) {
        progressBar.visibility = if (visible) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

}