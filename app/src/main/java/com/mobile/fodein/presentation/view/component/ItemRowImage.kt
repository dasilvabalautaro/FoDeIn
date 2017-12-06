package com.mobile.fodein.presentation.view.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import butterknife.BindView
import butterknife.ButterKnife
import com.mobile.fodein.R

class ItemRowImage: FrameLayout {
    constructor(context: Context): super(context)
    constructor(context: Context, attributeSet: AttributeSet):
            super(context, attributeSet)
    @BindView(R.id.iv_form)
    @JvmField var ivForm: ImageView? = null

    init {
        LayoutInflater.from(context)
                .inflate(R.layout.view_row_image, this, true)
        ButterKnife.bind(this)

    }
}