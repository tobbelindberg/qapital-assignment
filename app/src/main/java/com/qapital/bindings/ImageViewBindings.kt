package com.qapital.bindings

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


@BindingAdapter("imageUrl", "placeholder", requireAll = false)
fun ImageView.loadImage(imageUrl: String?, placeholder: Drawable?) {
    var request = Glide.with(this)
        .load(imageUrl)

    placeholder?.also {
        request.apply(RequestOptions().placeholder(placeholder))
    }

    request.into(this)
}