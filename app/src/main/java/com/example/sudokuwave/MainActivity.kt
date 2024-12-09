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
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        val padding: PaddingValues = PaddingValues(4.dp)
     //,  val fontFamily: FontFamily
    )
    /***/
    data class MenuConfig(
        val style: MenuStyle = MenuStyle(),
        val leftColumn:Boolean = false,
        val leftContent: List<MenuElement> = emptyList(),
        val centerContent: List<MenuElement> = emptyList(),
        val rightContent: List<MenuElement> = emptyList()
    )
    /***/
    /*
   val context = LocalContext.current
   val nomApp : String = context.getString(R.string.app_name)
 */
   var nomApp : String = "Sudoku Wave"
   var userNumber: String = "Nobody"
   // var idUser: Int = 0
    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val currentFragmentTag = sharedViewModel.stateFlowVariable.collectAsState().value



            SudokuWaveTheme {
                Scaffold(modifier = Modifier.fillMaxSize()
                ) { innerPadding ->

                    val menuConfig = getMenuConfigForFragment(currentFragmentTag)
                    Column(modifier = Modifier.fillMaxSize().background(color = Color.Gray).padding(innerPadding))
                    {
                        // Afficher le menu en haut du contenu
                        CustomMenu(
                            config = menuConfig,
                            onElementClick = { element ->
                                Log.d("Menu Click","Clicked on $element")
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


    private fun getMenuConfigForFragment(fragmentTag: String): MenuConfig {
        return when (fragmentTag) {
            "HomeFragment" -> MenuConfig(
                leftContent = listOf(MenuElement.TextItem(text="Home" ,isClickable = true),MenuElement.IconItem(Icons.Default.Settings,"Configuration",isClickable = true)),
                centerContent = listOf(
                    MenuElement.TextItem(
                        nomApp,
                        style = TextStyle(
                            fontFamily = FontFamily.Cursive,
                            fontStyle = FontStyle.Italic,
                            fontSize = 32.sp)),
                    //MenuElement.TextItem("\n"),
                   // MenuElement.TextItem("Settings App")
                ),
                rightContent = listOf(MenuElement.ButtonItem("Click Me") { println("Button clicked!") })
            )
            "SettingsFragment" -> MenuConfig(
                style = MenuStyle(
                    backgroundColor = Color.Gray,
                    textColor = Color.Black,
                    padding = PaddingValues(vertical = 16.dp)
                ),
                leftContent = listOf(MenuElement.TextItem("Settings", isClickable = true)),
                centerContent = listOf(MenuElement.TextItem("Settings App")),
                rightContent = listOf(MenuElement.IconItem(Icons.Default.Settings, "Settings"))
            )
            else -> MenuConfig()
        }
    }




    /***************************************************/
}



