package com.example.sudokuwave.tools
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sudokuwave.tools.ThemeRepository

class ThemeViewModel(private val repository: ThemeRepository) : ViewModel() {

    private val _selectedMenuColor = MutableLiveData<Int>()
    val selectedMenuColor: LiveData<Int> = _selectedMenuColor

    private val _unselectedMenuColor = MutableLiveData<Int>()
    val unselectedMenuColor: LiveData<Int> = _unselectedMenuColor

    init {
        val (selected, unselected) = repository.loadTheme()
        _selectedMenuColor.value = selected
        _unselectedMenuColor.value = unselected
    }

    fun setTheme(selectedColor: Int, unselectedColor: Int) {
        _selectedMenuColor.value = selectedColor
        _unselectedMenuColor.value = unselectedColor
        repository.saveTheme(selectedColor, unselectedColor)
    }
}