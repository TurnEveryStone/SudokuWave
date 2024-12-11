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
    //var alignment: Alignment
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(config.style.backgroundColor)
            .padding(config.style.padding)
    ) {
        // Section Gauche
        Row(
            modifier = Modifier
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
           // val alignment: AlignmentType= AlignmentType.Start
            BuildMenuContainer(
                container = config.leftContent,
                onElementClick = onElementClick,
                textColor = config.style.textColor,
                Alignment.Start

            )
        }

        // Section Centre
        Row(
            modifier = Modifier
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            BuildMenuContainer(
                container = config.centerContent,
                onElementClick = onElementClick,
                textColor = config.style.textColor,
                Alignment.CenterHorizontally
            )
        }

        // Section Droite
        Row(
            modifier = Modifier
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            BuildMenuContainer(
                container = config.rightContent,
                onElementClick = onElementClick,
                textColor = config.style.textColor,
                Alignment.End
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
fun BuildMenuContainer2(
    container: MenuContainer,
    onElementClick: (MenuElement) -> Unit,
    textColor: Color
) {
    when (container) {
        is MenuContainer.SingleItem -> {
            MenuElementComposable(container.element, onElementClick, textColor)
        }

        is MenuContainer.RowContainer -> {
            Row(modifier = Modifier.background(Color.Red),)
            {
                container.children.forEach { child ->
                    BuildMenuContainer(child, onElementClick, textColor)
                }
            }
        }

        is MenuContainer.ColumnContainer -> {
            Column(modifier = Modifier.background(Color.Yellow),
                verticalArrangement = Arrangement.spacedBy(8.dp),


            ) {
                container.children.forEach { child ->
                    BuildMenuContainer(child, onElementClick, textColor)
                }
            }
        }
    }
}
/*********************************************************/
@Composable
fun BuildMenuContainer(
    container: MenuContainer,
    onElementClick: (MenuElement) -> Unit,
    textColor: Color,
    parentAlignment: Alignment.Horizontal = Alignment.CenterHorizontally // Alignement hérité
) {
    when (container) {
        is MenuContainer.ColumnContainer -> {
            Column(
                modifier = Modifier.fillMaxWidth(),
               // verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = parentAlignment // Hérite de l'alignement du parent
            ) {
                container.children.forEach { child ->
                    BuildMenuContainer(child, onElementClick, textColor, parentAlignment)
                }
            }
        }
        is MenuContainer.RowContainer -> {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                container.children.forEach { child ->
                    BuildMenuContainer(child, onElementClick, textColor)
                }
            }
        }
        is MenuContainer.SingleItem -> {
            MenuElementComposable(container.element, onElementClick, textColor)
        }
    }
}
