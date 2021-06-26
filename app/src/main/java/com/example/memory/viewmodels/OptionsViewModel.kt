package com.example.memory.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.memory.R
import com.example.memory.data.Repository
import com.example.memory.data.Settings
import com.example.memory.ui.base.NavigEvent
import com.example.memory.ui.base.NavigateEventInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import javax.inject.Inject

@HiltViewModel
class OptionsViewModel @Inject public constructor(
    private val settingsRepository: Repository
): BaseViewModel() {
    val options = settingsRepository.getSettings().asLiveData()

    fun getLastSettings(): LiveData<Settings> {
        return options
    }

    fun saveSettings(theme: String, difficulty: String) {
        val settings = Settings(0, theme, difficulty)

        GlobalScope.async {
            settingsRepository.deletePreviousSettings()
            settingsRepository.settingsDao.insert(settings)
        }

        _navigateToFragment.value = NavigEvent(NavigateEventInfo((R.id.action_optionsFragment_to_mainMenuFragment)))
    }

    fun goToMainMenu() {
        _navigateToFragment.value = NavigEvent(NavigateEventInfo((R.id.action_optionsFragment_to_mainMenuFragment)))
    }
}