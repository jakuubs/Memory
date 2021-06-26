package com.example.memory.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.example.memory.R
import com.example.memory.databinding.FragmentMainMenuBinding
import com.example.memory.ui.base.BaseFragment
import com.example.memory.viewmodels.MainMenuViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainMenuFragment : BaseFragment() {

    var fragmentMainMenuBinding: FragmentMainMenuBinding? = null
    val mmViewModel: MainMenuViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentMainMenuBinding>(
            inflater, R.layout.fragment_main_menu, container, false
        )

        this.fragmentMainMenuBinding = binding

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentMainMenuBinding?.apply {
            lifecycleOwner = viewLifecycleOwner
            mainMenuViewModel = mmViewModel
        }
        observeModelNavigation(mmViewModel)
    }
}