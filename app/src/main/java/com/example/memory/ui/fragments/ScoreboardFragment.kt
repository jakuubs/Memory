package com.example.memory.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.example.memory.R
import com.example.memory.data.Score
import com.example.memory.databinding.FragmentScoreboardBinding
import com.example.memory.ui.base.BaseFragment
import com.example.memory.ui.fragments.adapters.ScoreAdapter
import com.example.memory.viewmodels.ScoreboardViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScoreboardFragment : BaseFragment() {

    lateinit var sAdapter: ScoreAdapter
    var fragmentScoreboardBinding: FragmentScoreboardBinding? = null
    val sViewModel: ScoreboardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentScoreboardBinding>(
            inflater, R.layout.fragment_scoreboard, container, false
        )

        this.fragmentScoreboardBinding = binding
        sAdapter = ScoreAdapter(sViewModel)

        subscribeUI(sAdapter)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentScoreboardBinding?.apply {
            lifecycleOwner = viewLifecycleOwner
            scoreboardRecyclerView.adapter = sAdapter
            scoreboardViewModel = sViewModel
        }
        observeModelNavigation(sViewModel)
    }

    private fun subscribeUI(newAdapter: ScoreAdapter) {
        sViewModel.scores.observe(viewLifecycleOwner) { result ->
            newAdapter.submitList(result.sortedWith(compareByDescending<Score> { it.points }.thenBy { it.time }))
            newAdapter.notifyDataSetChanged()
        }
    }
}