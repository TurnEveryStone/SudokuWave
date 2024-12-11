package com.example.sudokuwave.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.sudokuwave.MainActivity.MenuConfig


@Composable
fun CustomMenu(
    config: MenuConfig,
    onElementClick: (MenuElement) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(config.style.backgroundColor)
            .padding(config.style.padding)
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            // Left content
            BuildMenuContainer(
                container = config.leftContent,
                onElementClick = onElementClick,
                textColor = config.style.textColor
            )
        }
        // Center content
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            BuildMenuContainer(
                container = config.centerContent,
                onElementClick = onElementClick,
                textColor = config.style.textColor
            )
        }
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            // Right content
            BuildMenuContainer(
                container = config.rightContent,
                onElementClick = onElementClick,
                textColor = config.style.textColor
            )
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
                // textAlign = TextAlign.Center,
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

        is MenuElement.SwitchButtonItem -> {
            // element.checked by remember { mutableStateOf(true) }


            Button(onClick = element.onClick) {
                Text(text = element.text, color = textColor)
            }
        }
    }
}

@Composable
fun BuildMenuContainer(
    container: MenuContainer,
    onElementClick: (MenuElement) -> Unit,
    textColor: Color
) {
    when (container) {
        is MenuContainer.SingleItem -> {
            MenuElementComposable(container.element, onElementClick, textColor)
        }

        is MenuContainer.RowContainer -> {
            Row {
                container.children.forEach { child ->
                    BuildMenuContainer(child, onElementClick, textColor)
                }
            }
        }

        is MenuContainer.ColumnContainer -> {
            Column {
                container.children.forEach { child ->
                    BuildMenuContainer(child, onElementClick, textColor)
                }
            }
        }
    }
}
/*********************************************************/
