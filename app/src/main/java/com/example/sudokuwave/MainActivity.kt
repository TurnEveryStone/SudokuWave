package com.example.sudokuwave

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.sudokuwave.ui.CustomMenu
import com.example.sudokuwave.ui.MenuElement
import com.example.sudokuwave.ui.theme.SudokuWaveTheme
import viewmodel.SharedViewModel

/***************************************************/
class MainActivity : AppCompatActivity() {
    data class MenuStyle(
        val backgroundColor: Color = Color.White,
        val textColor: Color = Color.Black,
        val padding: PaddingValues = PaddingValues(4.dp),
    //    val fontFamily: FontFamily
    )
    var userNumber: String = "Nobody"
   // var idUser: Int = 0
    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val currentFragmentTag = remember { sharedViewModel.stateFlowVariable }

            SudokuWaveTheme {
                Scaffold(modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    val (menuStyle, menuConfig) = getMenuConfigForFragment(currentFragmentTag.value)
                    Column(modifier = Modifier.fillMaxSize().background(color = Color.Gray).padding(innerPadding))
                    {
                        // Afficher le menu en haut du contenu
                        CustomMenu(
                            style = menuStyle,
                            leftContent = menuConfig.first,
                            centerContent = menuConfig.second,
                            rightContent = menuConfig.third,
                            onElementClick = { element ->
                                println("Clicked on $element")
                            }
                        )

                        // Contenu principal
                        Text(text = "Hello $userNumber")
                        FragmentContainerComposable()
                        ObserveViewModel(sharedViewModel)
                    }
                }

            }
        }
    }
    /***************************************************/
    @Composable
    fun FragmentContainerComposable() {
        val fragmentManager = (LocalContext.current as AppCompatActivity).supportFragmentManager
        val containerId = remember { View.generateViewId() }
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                FrameLayout(context).apply { id = containerId }
            },
            update = { view ->
                if (fragmentManager.findFragmentById(containerId) == null) {
                    fragmentManager.beginTransaction()
                        .replace(containerId, HomeFragment())
                        .commit()
                }
            }
        )
    }
    /***************************************************/
    @Composable
    fun ObserveViewModel(sharedViewModel: SharedViewModel) {
        val value = sharedViewModel.stateFlowVariable.collectAsState().value
        Text(text = "La variable StateFlow a changé : $value")
    }
    /***************************************************/


    private fun getMenuConfigForFragment(fragmentTag: String): Pair<MenuStyle, Triple<List<MenuElement>, List<MenuElement>, List<MenuElement>>> {
        return when (fragmentTag) {
            "HomeFragment" -> MenuStyle(
                backgroundColor = Color.DarkGray,textColor = Color.White,padding = PaddingValues(horizontal = 4.dp)
            ) to Triple(
                listOf(MenuElement.IconItem(Icons.Default.Home, "Home", isClickable = true),MenuElement.IconItem(Icons.Default.Settings,"Configuration",isClickable = true)),
                listOf(MenuElement.TextItem("MyApp", isClickable = false)),
                listOf(MenuElement.ButtonItem("Click Me") { println("Button clicked!") })
            )
            "SettingsFragment" -> MenuStyle(
                backgroundColor = Color.Gray,textColor = Color.Black,padding = PaddingValues(vertical = 4.dp)
            ) to Triple(
                listOf(MenuElement.TextItem("Settings", isClickable = true)),
                listOf(MenuElement.TextItem("Settings App", isClickable = false)),
                listOf(MenuElement.IconItem(Icons.Default.Settings, "Settings"))
            )
            else -> MenuStyle() to Triple(emptyList(), emptyList(), emptyList())
        }
    }
    /***************************************************/
}



