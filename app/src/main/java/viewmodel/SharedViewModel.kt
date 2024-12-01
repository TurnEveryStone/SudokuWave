package com.example.sudokuwave.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SharedViewModel : ViewModel() {
    // Exemple avec StateFlow
    private val _stateFlowVariable = MutableStateFlow("Valeur initiale")
    val stateFlowVariable: StateFlow<String> = _stateFlowVariable

    // Méthode pour mettre à jour le StateFlow
    fun updateStateFlow(value: String) {
        _stateFlowVariable.value = value
    }
}