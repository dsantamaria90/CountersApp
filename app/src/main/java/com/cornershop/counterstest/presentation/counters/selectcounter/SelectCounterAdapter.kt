package com.cornershop.counterstest.presentation.counters.selectcounter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cornershop.counterstest.databinding.SelectCounterItemLayoutBinding

class SelectCounterAdapter(
    private val listener: SelectCounterClickListener,
    private val countersList: Array<String>
) : RecyclerView.Adapter<SelectCounterAdapter.ViewHolder>() {

    override fun getItemCount(): Int = countersList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            SelectCounterItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(countersList[position])
    }

    inner class ViewHolder(
        private val binding: SelectCounterItemLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(title: String) {
            binding.apply {
                this.title = title
                this.listener = this@SelectCounterAdapter.listener
            }
        }
    }
}