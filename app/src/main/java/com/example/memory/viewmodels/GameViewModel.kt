package com.example.memory.viewmodels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.example.memory.R
import com.example.memory.data.Repository
import com.example.memory.data.Score
import com.example.memory.data.Settings
import com.example.memory.ui.base.NavigEvent
import com.example.memory.ui.base.NavigateEventInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject public constructor(
    private val gameRepository: Repository,
): BaseViewModel() {

    val points: MutableLiveData<Long> = MutableLiveData()
    val time: MutableLiveData<Long> = MutableLiveData()
    val difficulty: MutableLiveData<String> = MutableLiveData()
    val date: MutableLiveData<String> = MutableLiveData()

    val settings = gameRepository.getSettings().asLiveData()

    fun getCurrentSettings(): LiveData<Settings> {
        return settings
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun finishGame(p: Int, t: Long, d: String, timePassed: Boolean) {
        if (timePassed)
            _navigateToFragment.value = NavigEvent(NavigateEventInfo((R.id.action_gameFragment_to_mainMenuFragment)))
        else {
            points.value = p.toLong()
            time.value = t
            difficulty.value = d
            date.value = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE)

            val newScore = Score(0, points.value!!, time.value!!, difficulty.value!!, date.value!!)
            GlobalScope.async {
                gameRepository.scoreDao.insert(newScore)
            }

            _navigateToFragment.value = NavigEvent(NavigateEventInfo((R.id.action_gameFragment_to_mainMenuFragment)))
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun playAgain(p: Int, t: Long, d: String, timePassed: Boolean) {
        if (timePassed)
            _navigateToFragment.value = NavigEvent(NavigateEventInfo((R.id.action_gameFragment_self)))
        else {
            points.value = p.toLong()
            time.value = t
            difficulty.value = d
            date.value = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE)

            val newScore = Score(0, points.value!!, time.value!!, difficulty.value!!, date.value!!)
            GlobalScope.async {
                gameRepository.scoreDao.insert(newScore)
            }

            _navigateToFragment.value = NavigEvent(NavigateEventInfo((R.id.action_gameFragment_self)))
        }
    }

    fun yes() {
        _navigateToFragment.value = NavigEvent(NavigateEventInfo((R.id.action_gameFragment_to_mainMenuFragment)))
    }
}