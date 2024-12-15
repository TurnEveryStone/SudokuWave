package com.example.sudokuwave.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class ButtonStyle(
    val backgroundColor: Color = Color.Unspecified,
    val textColor: Color = Color.Black,
    val padding: PaddingValues = PaddingValues(8.dp),
    val textStyle: TextStyle = TextStyle.Default,
    val width: Dp = Dp.Unspecified, // Largeur du bouton
    val height: Dp = Dp.Unspecified // Hauteur du bouton
)


sealed class MenuContainer {
    data class SingleItem(
        val element: MenuElement
    ) : MenuContainer()

    data class RowContainer(val children: List<MenuContainer>) : MenuContainer()
    data class ColumnContainer(val children: List<MenuContainer>) : MenuContainer()
}

sealed class MenuElement {
    data class TextItem(
        val text: String,
        val style: TextStyle = TextStyle.Default,
        val isClickable: Boolean = false,
        val actionKey: String? = null  // Permet de déclencher une action
    ) : MenuElement()

    data class IconItem(
        val icon: ImageVector,
        val color: Color,
        val contentDescription: String? = null,
        val isClickable: Boolean = false,
        val actionKey: String? = null
    ) : MenuElement()

    data class ImageItem(
        val imageRes: Int,
        val contentDescription: String? = null,
        val isClickable: Boolean = false,
        val actionKey: String? = null
    ) : MenuElement()

    data class ButtonItem(
        val text: String,
        val style: ButtonStyle = ButtonStyle(),
        val onClick: () -> Unit,
        val actionKey: String? = null
    ) : MenuElement()

    data class CheckboxItem(
        val text: String,
        val isChecked: Boolean,
        val onCheckedChange: (Boolean) -> Unit,
        val actionKey: String? = null
    ) : MenuElement()

    data class SwitchItem(
        val text: String,
        val isChecked: Boolean,
        val onCheckedChange: (Boolean) -> Unit,
        val actionKey: String? = null
    ) : MenuElement()
}
