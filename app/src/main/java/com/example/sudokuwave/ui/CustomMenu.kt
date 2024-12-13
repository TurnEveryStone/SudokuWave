package com.example.sudokuwave.ui


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp



@Composable
fun CustomMenu(
    config: MenuConfig,
    onElementClick: (MenuElement, String?) -> Unit, // Add actionKey parameter
    onAction: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(config.style.backgroundColor)
            .padding(4.dp, 2.dp, 4.dp, 0.dp)
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            BuildMenuContainer(
                container = config.leftContent,
                onElementClick = onElementClick,
                textColor = config.style.textColor,
                parentAlignment = Alignment.Start,
                onAction = onAction
            )
        }

        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            BuildMenuContainer(
                container = config.centerContent,
                onElementClick = onElementClick,
                textColor = config.style.textColor,
                parentAlignment = Alignment.CenterHorizontally,
                onAction = onAction
            )
        }

        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            BuildMenuContainer(
                container = config.rightContent,
                onElementClick = onElementClick,
                textColor = config.style.textColor,
                parentAlignment = Alignment.End,
                onAction = onAction
            )
        }
    }
}

/******************************************************/
@Composable
fun BuildMenuContainer(
    container: MenuContainer,
    onElementClick: (MenuElement, String?) -> Unit,
    textColor: Color,
    parentAlignment: Alignment.Horizontal,
    onAction: (String) -> Unit // Ajouter ce paramètre
) {
    when (container) {
        is MenuContainer.ColumnContainer -> {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = parentAlignment
            ) {
                container.children.forEach { child ->
                    BuildMenuContainer(child, onElementClick, textColor, parentAlignment, onAction)
                }
            }
        }

        is MenuContainer.RowContainer -> {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = alignmentToArrangement(parentAlignment)
            ) {
                container.children.forEach { child ->
                    BuildMenuContainer(child, onElementClick, textColor, parentAlignment, onAction)
                }
            }
        }

        is MenuContainer.SingleItem -> {
            MenuElementComposable(
                element = container.element, // Passe directement l'élément
                onClick = onElementClick,
                textColor = textColor, onAction
            )
        }

    }
}

@Composable
fun MenuElementComposable(
    element: MenuElement,
    onClick: (MenuElement, String?) -> Unit,
    textColor: Color,
    onAction: (String) -> Unit // Ajouter ce paramètre
) {
    when (element) {
        is MenuElement.TextItem -> {
            Text(
                text = element.text,
                style = element.style.copy(color = textColor),
                modifier = Modifier
                    .padding(4.dp)
                    .clickable(enabled = element.isClickable) {
                        onClick(element)
                        element.actionKey?.let(onAction) // Déclencher l'action si présente
                    }
            )
        }

        is MenuElement.IconItem -> {
            Box(
                modifier = Modifier.size(32.dp).background(Color.Transparent, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = {
                    if (element.isClickable) onClick(element)
                    element.actionKey?.let(onAction)
                }) {
                    Icon(
                        modifier = Modifier.size(28.dp),
                        imageVector = element.icon,
                        contentDescription = element.contentDescription,
                        tint = element.color
                    )
                }
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
        is MenuElement.CheckboxItem -> {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(4.dp)
            ) {
                Checkbox(
                    checked = element.isChecked,
                    onCheckedChange = { isChecked ->
                        element.onCheckedChange(isChecked)
                        element.actionKey?.let(onAction)
                    }
                )
                Text(text = element.text, modifier = Modifier.padding(start = 8.dp))
            }
        }

        is MenuElement.SwitchItem -> {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(4.dp)
            ) {
                Text(text = element.text, modifier = Modifier.padding(end = 8.dp))
                Switch(
                    checked = element.isChecked,
                    onCheckedChange = { isChecked ->
                        element.onCheckedChange(isChecked)
                        element.actionKey?.let(onAction)
                    }
                )
            }
        }
    }
}

fun alignmentToArrangement(alignment: Alignment.Horizontal): Arrangement.Horizontal {
    return when (alignment) {
        Alignment.Start -> Arrangement.Start
        Alignment.CenterHorizontally -> Arrangement.Center
        Alignment.End -> Arrangement.End
        else -> Arrangement.Start // Défaut au cas improbable
    }
}