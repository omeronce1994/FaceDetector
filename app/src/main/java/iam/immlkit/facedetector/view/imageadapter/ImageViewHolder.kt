package iam.immlkit.facedetector.view.imageadapter

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import iam.immlkit.facedetector.R
import iam.immlkit.facedetector.databinding.ItemImageBinding
import iam.immlkit.facedetector.model.ImageModel
import iam.immlkit.facedetector.utils.FileUtils
import java.io.File
import java.lang.Exception
import java.net.URI

class ImageViewHolder(private val binding: ItemImageBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ImageModel){
        binding?.apply {
            image = item
            executePendingBindings()
        }
    }
}