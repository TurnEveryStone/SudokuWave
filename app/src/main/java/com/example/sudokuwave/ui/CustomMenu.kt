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
import androidx.compose.ui.graphics.RectangleShape
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
            .padding(4.dp,2.dp,4.dp,0.dp)
        //    .padding(config.style.padding)
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
            Box(
                modifier = Modifier
                    .size(32.dp) // Taille du conteneur alignée aux autres éléments
                    .background(Color.Transparent,
                        shape = CircleShape) // Permet de visualiser l'espace utilisé
                    ,
                contentAlignment = Alignment.Center // Centre l'icône dans la Box
            ) {
                IconButton(
                   // modifier = Modifier.padding(0.dp),
                    onClick = { if (element.isClickable) onClick(element) }) {
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
                    .size(36.dp)
                    .padding(4.dp)
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

/*********************************************************/
@Composable
fun BuildMenuContainer(
    container: MenuContainer,
    onElementClick: (MenuElement) -> Unit,
    textColor: Color,
    parentAlignment: Alignment.Horizontal // Alignement hérité
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
                horizontalArrangement = alignmentToArrangement(parentAlignment)
            ) {
                container.children.forEach { child ->
                    BuildMenuContainer(child, onElementClick, textColor, parentAlignment)
                }
            }
        }

        is MenuContainer.SingleItem -> {
            MenuElementComposable(container.element, onElementClick, textColor)
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