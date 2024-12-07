package com.example.sudokuwave.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.sudokuwave.MainActivity.MenuStyle

@Composable
fun CustomMenu(
    style: MenuStyle,
    leftContent: List<MenuElement>,
    centerContent: List<MenuElement>,
    rightContent: List<MenuElement>,
    onElementClick: (MenuElement) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(style.backgroundColor)
            .padding(style.padding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Left content
        Row(verticalAlignment = Alignment.CenterVertically) {
            leftContent.forEach { element ->
                MenuElementComposable(element, onElementClick, style.textColor)
            }
        }

        // Center content
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            centerContent.forEach { element ->
                MenuElementComposable(element, onElementClick, style.textColor)
            }
        }

        // Right content
        Row(verticalAlignment = Alignment.CenterVertically) {
            rightContent.forEach { element ->
                MenuElementComposable(element, onElementClick, style.textColor)
            }
        }
    }
}

@Composable
fun MenuElementComposable(element: MenuElement, onClick: (MenuElement) -> Unit, textColor: Color) {
    when (element) {
        is MenuElement.TextItem -> {
            Text(
                text = element.text,
                style = element.style.copy(color = textColor),
                modifier = Modifier
                    .padding(4.dp)
                    .clickable(enabled = element.isClickable) { onClick(element) }
            )
        }
        is MenuElement.IconItem -> {
            IconButton(onClick = { if (element.isClickable) onClick(element) }) {
                Icon(
                    imageVector = element.icon,
                    contentDescription = element.contentDescription,
                    tint = textColor
                )
            }
        }
        is MenuElement.ImageItem -> {
            Image(
                painter = painterResource(id = element.imageRes),
                contentDescription = element.contentDescription,
                modifier = Modifier
                    .size(24.dp)
                    .clickable(enabled = element.isClickable) { onClick(element) }
            )
        }
        is MenuElement.ButtonItem -> {
            Button(onClick = element.onClick) {
                Text(text = element.text, color = textColor)
            }
        }
    }
}
