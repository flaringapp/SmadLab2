package com.flaringapp.smadlab2.presentation.fragments.home.impl.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.flaringapp.smadlab2.R
import com.flaringapp.smadlab2.presentation.fragments.home.HomeContract
import kotlinx.android.synthetic.main.view_holder_interval.view.*

class IntervalViewHolder private constructor(view: View) : RecyclerView.ViewHolder(view) {

    companion object {
        fun create(parent: ViewGroup): IntervalViewHolder {
            return IntervalViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.view_holder_interval, parent, false)
            )
        }
    }

    fun bind(model: HomeContract.IIntervalViewModel) {
        itemView.apply {
            textInterval.text = model.interval
            textAverage.text = model.average
            textFrequency.text = model.frequency
        }
    }

}