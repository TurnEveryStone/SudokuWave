package viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SharedViewModel : ViewModel() {
    // StateFlow pour Jetpack Compose
    private val _stateFlowVariable = MutableStateFlow("Valeur initiale")
    val stateFlowVariable: StateFlow<String> = _stateFlowVariable

    fun updateStateFlow(value: String) {
        _stateFlowVariable.value = value
    }

    // LiveData pour les Fragments classiques
    private val _liveDataVariable = MutableLiveData<String>("Valeur initiale")
    val liveDataVariable: LiveData<String> = _liveDataVariable

    fun updateLiveData(value: String) {
        _liveDataVariable.value = value
    }
}