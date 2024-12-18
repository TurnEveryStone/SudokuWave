package com.example.sudokuwave.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sudokuwave.ui.theme.Beige

data class MenuStyle(
    val backgroundColor: Color = Color.White,
    val textColor: Color = Color.Black,
    val padding: PaddingValues = PaddingValues(2.dp)
    //,  val fontFamily: FontFamily
)

/***/

data class MenuConfig(
    val style: MenuStyle = MenuStyle(),
    val leftContent: MenuContainer = MenuContainer.RowContainer(emptyList()),
    val centerContent: MenuContainer = MenuContainer.RowContainer(emptyList()),
    val rightContent: MenuContainer = MenuContainer.RowContainer(emptyList())
)

fun getMenuConfig(menu: String): MenuConfig {
    return when (menu) {
        "Home" -> {
            MenuConfig(
                style = MenuStyle(
                    backgroundColor = Beige,
                    textColor = Color.Black,
                    padding = PaddingValues(2.dp)
                ),
                leftContent = MenuContainer.ColumnContainer(
                    listOf(
                        MenuContainer.SingleItem(
                            MenuElement.IconItem(
                                Icons.Default.Person,
                                color = Color.Blue,
                                contentDescription = "Person",
                                isClickable = true,
                                actionKey = "Settings"
                            )
                        )
                    )
                ),
                centerContent = MenuContainer.ColumnContainer(
                    listOf(
                        MenuContainer.SingleItem(
                            MenuElement.TextItem(
                                "Le Sudoku",
                                style = TextStyle(
                                    fontFamily = FontFamily.Cursive,
                                    fontStyle = FontStyle.Italic,
                                    fontSize = 24.sp
                                )
                            )
                        ),
                        MenuContainer.SingleItem(
                            MenuElement.ButtonItem(
                                text = "Inviter des amis",
                                onClick = { /* Action à réaliser */ },
                                actionKey = "GoToAdvancedSettings",
                                style = ButtonStyle(
                                    backgroundColor = Color.DarkGray, // Fond du bouton
                                    textColor = Color.White, // Couleur du texte
                                    padding = PaddingValues(0.dp, 0.dp, 0.dp, 2.dp), // Espacement
                                    textStyle = TextStyle(
                                        fontFamily = FontFamily.Serif,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    width = 180.dp, // Largeur du bouton
                                    height = 25.dp // Hauteur du bouton
                                )
                            )

                        )
                    )
                ),
                rightContent = MenuContainer.ColumnContainer(
                    listOf(
                        MenuContainer.RowContainer(
                            listOf(
                                MenuContainer.SingleItem(
                                    MenuElement.IconItem(
                                        Icons.Default.ShoppingCart,
                                        color = Color.DarkGray,
                                        contentDescription = "Basket",
                                        isClickable = true,
                                        actionKey = "Settings"
                                    )
                                ),
                                MenuContainer.SingleItem(
                                    MenuElement.IconItem(
                                        Icons.Default.Settings,
                                        color = Color.DarkGray,
                                        contentDescription = "Settings",
                                        isClickable = true,
                                        actionKey = "Settings"
                                    )
                                )
                            )
                        )
                    )
                )
            )
        }


        "Settings" -> {
            MenuConfig(
                style = MenuStyle(
                    backgroundColor = Color.LightGray,
                    textColor = Color.Black,
                    padding = PaddingValues(4.dp)
                ),
                leftContent = MenuContainer.RowContainer(
                    listOf(
                        MenuContainer.SingleItem(
                            MenuElement.TextItem(
                                text = "Settings",
                                isClickable = false
                            )
                        )
                    )
                ),
                centerContent = MenuContainer.ColumnContainer(
                    listOf(
                        MenuContainer.SingleItem(
                            MenuElement.CheckboxItem(
                                text = "Enable Notifications",
                                isChecked = true,
                                onCheckedChange = { isChecked ->
                                    println("Notifications toggled: $isChecked")
                                }
                            )
                        ),
                        MenuContainer.SingleItem(
                            MenuElement.SwitchItem(
                                text = "Dark Mode",
                                isChecked = false,
                                onCheckedChange = { isChecked ->
                                    println("Dark Mode toggled: $isChecked")
                                }
                            )
                        )
                    )
                ),
                rightContent = MenuContainer.RowContainer(
                    listOf(
                        MenuContainer.SingleItem(
                            MenuElement.IconItem(
                                icon = Icons.Default.Settings,
                                color = Color.Gray,
                                contentDescription = "Settings Icon",
                                isClickable = true,
                                actionKey = "GoToAdvancedSettings"
                            )
                        )
                    )
                )
            )
        }


        else -> {
            MenuConfig(
                style = MenuStyle(
                    backgroundColor = Color.LightGray,
                    textColor = Color.Black,
                    padding = PaddingValues(4.dp)
                ),
                centerContent = MenuContainer.RowContainer(
                    listOf(
                        MenuContainer.SingleItem(
                            MenuElement.TextItem(
                                "Default Menu", style = TextStyle(
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        )
                    )
                )
            )
        }
    }
}
