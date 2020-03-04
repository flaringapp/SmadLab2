package com.flaringapp.smadlab2.presentation.fragments.home.impl.adapter

import androidx.recyclerview.widget.DiffUtil
import com.flaringapp.smadlab2.presentation.fragments.home.HomeContract

class IntervalsDiffUtilCallback(
    private val oldItems: List<HomeContract.IIntervalViewModel>,
    private val newItems: List<HomeContract.IIntervalViewModel>
): DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldItems.size

    override fun getNewListSize(): Int = newItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition].interval == newItems[newItemPosition].interval
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition].average == newItems[newItemPosition].average &&
                oldItems[oldItemPosition].frequency == newItems[newItemPosition].frequency
    }
}