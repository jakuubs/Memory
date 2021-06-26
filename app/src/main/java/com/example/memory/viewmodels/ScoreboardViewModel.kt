package com.example.memory.viewmodels

import androidx.lifecycle.asLiveData
import com.example.memory.R
import com.example.memory.data.Repository
import com.example.memory.ui.base.NavigEvent
import com.example.memory.ui.base.NavigateEventInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ScoreboardViewModel @Inject public constructor(
    private val scoreRepository: Repository
): BaseViewModel() {
    val scores = scoreRepository.getAllScores().asLiveData()

    fun goToMainMenu() {
        _navigateToFragment.value = NavigEvent(NavigateEventInfo((R.id.action_scoreboardFragment_to_mainMenuFragment)))
    }
}