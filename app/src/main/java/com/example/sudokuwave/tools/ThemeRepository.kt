package com.example.sudokuwave.tools

import android.content.Context
import com.example.sudokuwave.R

class ThemeRepository(private val context: Context) {

    private val preferences = context.getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)

    fun saveTheme(selectedColor: Int, unselectedColor: Int) {
        preferences.edit()
            .putInt("selectedMenuColor", selectedColor)
            .putInt("unselectedMenuColor", unselectedColor)
            .apply()
    }

    fun loadTheme(): Pair<Int, Int> {
        val selectedColor = preferences.getInt("selectedMenuColor", R.color.default_selected_color)
        val unselectedColor = preferences.getInt("unselectedMenuColor", R.color.default_unselected_color)
        return Pair(selectedColor, unselectedColor)
    }
}
