package com.example.github_profile.utils

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.github_profile.R
import com.kennyc.view.MultiStateView

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun emptyString() = ""

fun MultiStateView.showDefaultState() {
    this.viewState = MultiStateView.ViewState.CONTENT
}

fun MultiStateView.showEmptyState() {
    this.viewState = MultiStateView.ViewState.EMPTY
}

fun MultiStateView.showLoadingState() {
    this.viewState = MultiStateView.ViewState.LOADING
}

fun MultiStateView.showErrorState(
    errorMessage: String? = null,
    title: String? = null,
    drawable: Drawable? = null,
    errorAction: (() -> Unit)? = null
) {
    this.viewState = MultiStateView.ViewState.ERROR

    errorMessage?.let {
        val tvError =
            this.getView(MultiStateView.ViewState.ERROR)?.findViewById<TextView>(R.id.tv_error)
        tvError?.text = errorMessage
    }

    title?.let {
        val tvTitle =
            this.getView(MultiStateView.ViewState.ERROR)?.findViewById<TextView>(R.id.tv_title)
        tvTitle?.text = title
    }

    drawable?.let {
        val imgError =
            this.getView(MultiStateView.ViewState.ERROR)?.findViewById<ImageView>(R.id.img_error)
        imgError?.setImageDrawable(drawable)
    }

    val btnError =
        this.getView(MultiStateView.ViewState.ERROR)?.findViewById<Button>(R.id.btn_error)

    btnError?.setOnClickListener { errorAction?.invoke() }
}