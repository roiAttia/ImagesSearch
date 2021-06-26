package roiattia.com.imagessearch.ui.search_images

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.list_item_image.view.*
import roiattia.com.imagessearch.R
import roiattia.com.imagessearch.data.domain_model.Image

class ImagesAdapter(
    private var images: List<Image>
) : RecyclerView.Adapter<ImagesAdapter.CriteriaViewHolder>() {

    fun setData(images: List<Image>) {
        this.images = images
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CriteriaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_image, parent, false)
        return CriteriaViewHolder(view)
    }

    override fun onBindViewHolder(holder: CriteriaViewHolder, position: Int) {
        val image = images[position]
        holder.bindImage(image)
    }

    override fun getItemCount(): Int = images.size

    class CriteriaViewHolder(private val view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {

        }

        fun bindImage(image: Image) {
            Glide.with(view)
                .load(image.largeImageUrl)
                .into(view.imageView)

        }
    }

}