package com.flaringapp.smadlab2.presentation.fragments.home.impl.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.flaringapp.smadlab2.presentation.fragments.home.HomeContract

class IntervalsAdapter : RecyclerView.Adapter<IntervalViewHolder>() {

    private val items: MutableList<HomeContract.IIntervalViewModel> = mutableListOf()

    fun setItems(items: List<HomeContract.IIntervalViewModel>) {
        val diffResult = DiffUtil.calculateDiff(
            IntervalsDiffUtilCallback(this.items, items)
        )

        this.items.clear()
        this.items.addAll(items)

        diffResult.dispatchUpdatesTo(this)
    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IntervalViewHolder {
        return IntervalViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: IntervalViewHolder, position: Int) {
        holder.bind(items[position])
    }
}