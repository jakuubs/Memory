package com.example.memory.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.memory.R
import com.example.memory.data.Settings
import com.example.memory.databinding.FragmentOptionsBinding
import com.example.memory.ui.base.BaseFragment
import com.example.memory.viewmodels.OptionsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OptionsFragment : BaseFragment() {

    var fragmentOptionsBinding: FragmentOptionsBinding? = null
    val oViewModel: OptionsViewModel by viewModels()

    lateinit var lastSettings: Settings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val fragmentBinding = DataBindingUtil.inflate<FragmentOptionsBinding>(
            inflater, R.layout.fragment_options, container, false
        )

        val spinnerIcons: Spinner = fragmentBinding.changeIconsSpinner

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.icons,
            R.layout.spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_item)
            spinnerIcons.adapter = adapter
        }

        val spinnerDifficulty: Spinner = fragmentBinding.changeDifficultySpinner

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.difficulty,
            R.layout.spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_item)
            spinnerDifficulty.adapter = adapter
        }

        var spinnerIconsNum = 2
        var spinnerDifficultyLevel = 1

        oViewModel.getLastSettings().observe(viewLifecycleOwner, Observer {
            lastSettings = it
            if (lastSettings.theme == "Animals")
                spinnerIconsNum = 0
            else if (lastSettings.theme == "Cars")
                spinnerIconsNum = 1
            else if (lastSettings.theme == "Emojis")
                spinnerIconsNum = 2
            else if (lastSettings.theme == "Fruit")
                spinnerIconsNum = 3
            else if (lastSettings.theme == "Logos")
                spinnerIconsNum = 4

            spinnerIcons.setSelection(spinnerIconsNum)

            if (lastSettings.difficulty == "Easy")
                spinnerDifficultyLevel = 0
            else if (lastSettings.difficulty == "Normal")
                spinnerDifficultyLevel = 1
            else if (lastSettings.difficulty == "Hard")
                spinnerDifficultyLevel = 2
            else if (lastSettings.difficulty == "Hardcore")
                spinnerDifficultyLevel = 3

            spinnerDifficulty.setSelection(spinnerDifficultyLevel)
        })

        this.fragmentOptionsBinding = fragmentBinding

        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentOptionsBinding?.apply {
            lifecycleOwner = viewLifecycleOwner
            optionsViewModel = oViewModel
        }
        observeModelNavigation(oViewModel)

        fragmentOptionsBinding!!.saveSettings.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                val spinIcons = fragmentOptionsBinding!!.changeIconsSpinner.selectedItem.toString()
                val diffIcons = fragmentOptionsBinding!!.changeDifficultySpinner.selectedItem.toString()
                oViewModel.saveSettings(spinIcons, diffIcons)
                Toast.makeText(context, "You've saved new game settings.", Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return super.onContextItemSelected(item)
    }
}