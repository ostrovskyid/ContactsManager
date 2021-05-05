package org.example.contactsmanager.utils

import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import org.example.contactsmanager.R


@BindingAdapter("imageUrl")
fun AppCompatImageView.setImageUrl(url: String?) {
    val imageView = this
    Glide.with(context).clear(this)
    RequestOptions().apply {
        this.placeholder(R.drawable.ic_no_image_with_title)
    }.run {
        Glide.with(imageView)
            .load(url)
            .centerCrop()
            .apply(this)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
    }
}