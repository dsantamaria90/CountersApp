package com.cornershop.counterstest.presentation.counters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cornershop.counterstest.databinding.CounterItemLayoutBinding
import com.cornershop.counterstest.domain.entity.Counter

class CountersAdapter(
    private val listener: CounterItemClickListener
) : RecyclerView.Adapter<CountersAdapter.ViewHolder>() {

    var countersList = emptyList<Counter>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = countersList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            CounterItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(countersList[position])
    }

    inner class ViewHolder(
        private val binding: CounterItemLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(counter: Counter) {
            binding.apply {
                this.counter = counter
                this.listener = this@CountersAdapter.listener
            }
        }
    }
}