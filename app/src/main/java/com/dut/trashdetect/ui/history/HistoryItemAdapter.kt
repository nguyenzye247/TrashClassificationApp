package com.dut.trashdetect.ui.history

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.dut.trashdetect.databinding.ItemHistoryBinding
import com.dut.trashdetect.model.UserResult
import com.dut.trashdetect.util.DateFormatUtils
import com.dut.trashdetect.util.Helpers.roundPercent

class HistoryItemAdapter(
    private val results: ArrayList<UserResult>
) : RecyclerView.Adapter<HistoryItemAdapter.HistoryItemViewHolder>() {

    inner class HistoryItemViewHolder(val binding: ItemHistoryBinding) : ViewHolder(binding.root) {
        fun bind(result: UserResult) {
            binding.apply {
                tvResultClass.text = result.result.name
                tvDate.text = DateFormatUtils.formatDateTime(result.createdAt)
                Glide.with(root.context)
                    .load(result.imageUrl)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            progressBarItem.isVisible = false
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            progressBarItem.isVisible = false
                            return false
                        }

                    })
                    .into(ivResult)
                tvPercentage.text = roundPercent(result.result.percentage)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryItemViewHolder {
        return HistoryItemViewHolder(
            ItemHistoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: HistoryItemViewHolder, position: Int) {
        holder.bind(results[position])
    }

    override fun getItemCount() = results.size
}
