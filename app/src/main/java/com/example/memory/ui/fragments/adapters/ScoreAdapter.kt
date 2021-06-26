package com.example.memory.ui.fragments.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.memory.R
import com.example.memory.data.Score
import com.example.memory.databinding.ScoreItemBinding
import com.example.memory.viewmodels.ScoreboardViewModel

class ScoreAdapter internal constructor(
    private val scoreboardViewModel: ScoreboardViewModel
): ListAdapter<Score, ScoreAdapter.ScoreViewHolder>(ScoreDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreViewHolder {
        return ScoreViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ScoreViewHolder, position: Int) {
        val score = getItem(position)
        holder.bind(score, scoreboardViewModel)
    }

    class ScoreViewHolder(val binding: ScoreItemBinding):
            RecyclerView.ViewHolder(binding.root) {

        fun bind(currentScore: Score, scoreboardViewModel: ScoreboardViewModel) {
            binding.score = currentScore
            binding.scoreboardViewModel = scoreboardViewModel
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ScoreViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding: ScoreItemBinding = DataBindingUtil.inflate(
                    layoutInflater, R.layout.score_item,
                    parent,
                    false
                )
                return ScoreViewHolder(binding)
            }
        }
    }
}

private class ScoreDiffCallback: DiffUtil.ItemCallback<Score>() {

    override fun areItemsTheSame(oldItem: Score, newItem: Score): Boolean {
        return oldItem.uid == newItem.uid
    }

    override fun areContentsTheSame(oldItem: Score, newItem: Score): Boolean {
        return oldItem == newItem
    }
}