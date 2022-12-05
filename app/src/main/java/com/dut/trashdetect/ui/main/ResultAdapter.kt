package com.dut.trashdetect.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.dut.trashdetect.databinding.ItemClassificationBinding
import com.dut.trashdetect.util.Helpers

class ResultAdapter(
    private val results: ArrayList<com.dut.trashdetect.model.Result>
) : RecyclerView.Adapter<ResultAdapter.ResultViewHolder>() {

    inner class ResultViewHolder(val binding: ItemClassificationBinding) :
        ViewHolder(binding.root) {

        fun bind(result: com.dut.trashdetect.model.Result) {
            binding.apply {
                val numb = (adapterPosition + 1).toString() + "."
                tvNumb.text = numb
                tvClass.text = result.name
                val textPercent = Helpers.roundPercent(result.percentage)
                tvPercentage.text = textPercent
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        return ResultViewHolder(
            ItemClassificationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        holder.bind(results[position])
    }

    override fun getItemCount() = results.size
}
