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

class ImageViewHolder(parent:ViewGroup): RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(
    R.layout.item_image,parent,false)) {

    var binding: ItemImageBinding? = null

    init {
        binding = DataBindingUtil.bind(itemView)
    }

    fun bind(item: ImageModel){
        val context = itemView.context
        val file = File(item.path)
        val uri = FileUtils.getDownloadFolderUri(context,file)
        //just load image to view (no need to consider ratio in our case)
        Picasso.get().load(uri).fit().into(binding?.ivImage)
    }
}