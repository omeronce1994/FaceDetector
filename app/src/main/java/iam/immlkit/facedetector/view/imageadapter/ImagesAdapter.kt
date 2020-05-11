package iam.immlkit.facedetector.view.imageadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import iam.immlkit.facedetector.R
import iam.immlkit.facedetector.databinding.ItemImageBinding
import iam.immlkit.facedetector.model.ImageModel

class ImagesAdapter(var clickListener: (image: ImageModel) -> Unit = {}) : PagedListAdapter<ImageModel, ImageViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding: ItemImageBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
            R.layout.item_image, parent, false)
        val viewHolder = ImageViewHolder(binding)
        binding.root.setOnClickListener{
            val item = getItem(viewHolder.adapterPosition)
            item?.let { clickListener(it) }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val curr = getItem(position)
        curr?.let {
            holder.bind(it)
        }
    }

    companion object {
        /**
         * This diff callback informs the PagedListAdapter how to compute list differences when new
         * PagedLists arrive.
         * <p>
         * For example, hen we will update single Image Path, the adapter will know to compute (using below methods) that only one item changed and change only it's
         * corresponding view
         * @see android.support.v7.util.DiffUtil
         */
        private val diffCallback = object : DiffUtil.ItemCallback<ImageModel>() {
            override fun areItemsTheSame(oldItem: ImageModel, newItem: ImageModel): Boolean =
                oldItem.path == newItem.path

            override fun areContentsTheSame(oldItem: ImageModel, newItem: ImageModel): Boolean =
                oldItem.path == newItem.path
        }
    }
}