package com.mobile.fodein.presentation.view.component

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import com.mobile.fodein.R
import com.mobile.fodein.tools.DataListDiffCallback


class ImageAdapter:
    RecyclerView.Adapter<ImageAdapter.ViewHolder>(){
    private val items: ArrayList<ImageView> = ArrayList()

    override fun onBindViewHolder(holder: ImageAdapter.ViewHolder?, position: Int) =
            holder!!.bind(items[position])

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup?,
                                    viewType: Int): ViewHolder {
        val view: ItemRowImage = LayoutInflater.from(parent!!.context)
                .inflate(R.layout.view_image,
                        parent, false) as ItemRowImage
        return ViewHolder(view)
    }

    fun setObjectList(itemList: ArrayList<ImageView>){
        val diffResult: DiffUtil.DiffResult = DiffUtil
                .calculateDiff(DataListDiffCallback(this.items, itemList))
        this.items.clear()
        this.items.addAll(itemList)
        diffResult.dispatchUpdatesTo(this)
    }


    class ViewHolder(private val itemRowImage: ItemRowImage):
            RecyclerView.ViewHolder(itemRowImage){
        fun bind(item: ImageView) = with(itemRowImage){
            val img: Bitmap = (item.drawable as BitmapDrawable).bitmap
            val resize = Bitmap.createScaledBitmap(img, (img.width*0.2).toInt(),
                    (img.height*0.2).toInt(), true)
            ivForm!!.setImageBitmap(resize)
        }

    }
}