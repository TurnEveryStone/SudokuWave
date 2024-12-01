package com.example.sudokuwave

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.sudokuwave.viewmodel.SharedViewModel

class HomeFragment : Fragment(R.layout.fragment_home) {
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observer LiveData
        sharedViewModel.liveDataVariable.observe(viewLifecycleOwner) { value ->
            println("Fragment : La variable LiveData a changé : $value")
        }

        // Modifier les données
        view.findViewById<Button>(R.id.button_update).setOnClickListener {
            sharedViewModel.updateLiveData("Nouvelle valeur depuis HomeFragment")
        }
    }
}