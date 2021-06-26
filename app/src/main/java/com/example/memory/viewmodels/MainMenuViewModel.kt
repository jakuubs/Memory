package com.example.memory.viewmodels

import com.example.memory.R
import com.example.memory.data.Repository
import com.example.memory.ui.base.NavigEvent
import com.example.memory.ui.base.NavigateEventInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainMenuViewModel @Inject public constructor(
    private val playerRepository: Repository
): BaseViewModel() {

    fun play() {
        _navigateToFragment.value = NavigEvent(NavigateEventInfo((R.id.action_mainMenuFragment_to_gameFragment)))
    }

    fun goToOptions() {
        _navigateToFragment.value = NavigEvent(NavigateEventInfo((R.id.action_mainMenuFragment_to_optionsFragment)))
    }

    fun showScoreboard() {
        _navigateToFragment.value = NavigEvent(NavigateEventInfo((R.id.action_mainMenuFragment_to_scoreboardFragment)))
    }
}