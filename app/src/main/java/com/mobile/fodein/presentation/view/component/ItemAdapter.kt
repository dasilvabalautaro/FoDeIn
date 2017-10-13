package com.mobile.fodein.presentation.view.component

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mobile.fodein.R
import com.mobile.fodein.domain.interfaces.IEntity
import com.mobile.fodein.tools.DataListDiffCallback


class ItemAdapter(private val listener: (IEntity) -> Unit):
        RecyclerView.Adapter<ItemAdapter.ViewHolder>() {
    private val items: ArrayList<IEntity> = ArrayList()

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder?, position: Int)=
            holder!!.bind(items[position], listener)

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int):
            ViewHolder{
        val view: ItemRowView = LayoutInflater.from(parent!!.context)
                .inflate(R.layout.view_item,
                        parent, false) as ItemRowView
        return ViewHolder(view)

    }

    fun setWeighingList(itemList: ArrayList<IEntity>){
        val diffResult: DiffUtil.DiffResult = DiffUtil
                .calculateDiff(DataListDiffCallback(this.items, itemList))
        this.items.clear()
        this.items.addAll(itemList)
        diffResult.dispatchUpdatesTo(this)
    }


    class ViewHolder(private val itemRowView: ItemRowView) :
            RecyclerView.ViewHolder(itemRowView) {

        fun bind(item: IEntity, listener: (IEntity) -> Unit) = with(itemRowView) {
            ivGraph!!.setImageResource(item.graph)
            ibLink!!.setImageResource(item.imageLink)
            tvTitle!!.text = item.title
            tvDescription!!.text = item.description
            setOnClickListener { listener(item) }
        }
    }
}