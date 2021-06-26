package com.example.memory.viewmodels

import com.example.memory.ui.base.NavigEvent
import com.example.memory.ui.base.NavigateEventInfo


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {
    internal val _navigateToFragment = MutableLiveData<NavigEvent<NavigateEventInfo>>()
    val navigateToFragment: LiveData<NavigEvent<NavigateEventInfo>>
        get() = _navigateToFragment

}
