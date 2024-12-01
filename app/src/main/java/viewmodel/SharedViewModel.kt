package com.example.sudokuwave.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SharedViewModel : ViewModel() {
    // Exemple avec LiveData
    val liveDataVariable = MutableLiveData<String>("Valeur initiale")

    // Exemple avec StateFlow
    private val _stateFlowVariable = MutableStateFlow("Valeur initiale")
    val stateFlowVariable: StateFlow<String> = _stateFlowVariable

    fun updateStateFlow(value: String) {
        _stateFlowVariable.value = value
    }

    fun updateLiveData(value: String) {
        liveDataVariable.value = value
    }
}
