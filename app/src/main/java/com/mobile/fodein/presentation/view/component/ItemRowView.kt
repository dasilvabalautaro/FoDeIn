package com.mobile.fodein.presentation.view.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.mobile.fodein.R


class ItemRowView : FrameLayout {

    constructor(context: Context): super(context)
    constructor(context: Context, attributeSet: AttributeSet):
            super(context, attributeSet)

    @BindView(R.id.iv_graph_left)
    @JvmField var ivGraph: ImageView? = null
    @BindView(R.id.tv_title)
    @JvmField var tvTitle: TextView? = null
    @BindView(R.id.tv_description)
    @JvmField var tvDescription: TextView? = null
    @BindView(R.id.ib_link)
    @JvmField var ibLink: ImageButton? = null

    init {
        LayoutInflater.from(context)
                .inflate(R.layout.view_item_data, this, true)
        ButterKnife.bind(this)

    }


}