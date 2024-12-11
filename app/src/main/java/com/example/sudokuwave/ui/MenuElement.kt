package com.example.sudokuwave.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle

sealed class MenuContainer {
    data class SingleItem(val element: MenuElement) : MenuContainer()
    data class RowContainer(val children: List<MenuContainer>) : MenuContainer()
    data class ColumnContainer(val children: List<MenuContainer>) : MenuContainer()
}
sealed class MenuElement {
    data class TextItem(

        val text: String,
        val style: TextStyle = TextStyle.Default,
        val isClickable: Boolean = false
    ) : MenuElement()

    data class IconItem(
        val icon: ImageVector,
        val contentDescription: String?,
        val isClickable: Boolean = false
    ) : MenuElement()

    data class ImageItem(
        val imageRes: Int,
        val contentDescription: String?,
        val isClickable: Boolean = false
    ) : MenuElement()

    data class ButtonItem(
        val text: String,
        val onClick: () -> Unit
    ) : MenuElement()
    data class SwitchButtonItem(
        var checked: Boolean,
        val text: String,
        val onClick: () -> Unit,
    ) : MenuElement()
}
