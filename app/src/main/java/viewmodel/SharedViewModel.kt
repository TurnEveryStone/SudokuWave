package com.example.sudokuwave.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class SharedViewModel : ViewModel() {
    private val _stateFlowVariable = MutableStateFlow("Valeur initiale")
    val stateFlowVariable: StateFlow<String> = _stateFlowVariable

    fun updateStateFlow(value: String) {
        _stateFlowVariable.value = value
    }
}
