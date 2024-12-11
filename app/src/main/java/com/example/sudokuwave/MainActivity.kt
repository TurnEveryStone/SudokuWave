package com.example.sudokuwave

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.sudokuwave.ui.CustomMenu
import com.example.sudokuwave.ui.MenuContainer
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
        val leftContent: MenuContainer = MenuContainer.RowContainer(emptyList()),
        val centerContent: MenuContainer = MenuContainer.RowContainer(emptyList()),
        val rightContent: MenuContainer = MenuContainer.RowContainer(emptyList())
    )

    var nomApp : String = "Sudoku Wave"
    var userNumber: String = "Nobody"
    /***/
    /*
   val context = LocalContext.current
   val nomApp : String = context.getString(R.string.app_name)
 */
    val menuConfig = MenuConfig(
        style = MenuStyle(
            backgroundColor = Color.White,
            textColor = Color.Black,
            padding = PaddingValues(2.dp)
        ),
        leftContent = MenuContainer.ColumnContainer(
            listOf(
                MenuContainer.SingleItem(MenuElement.TextItem("Left Item", isClickable = true)),
                MenuContainer.SingleItem(MenuElement.TextItem("Clickable", isClickable = true))
            )
        ),
        centerContent = MenuContainer.ColumnContainer(
            listOf(
                MenuContainer.SingleItem(MenuElement.TextItem(nomApp,
                    style = TextStyle(
                        fontFamily = FontFamily.Cursive,
                        fontStyle = FontStyle.Italic,
                        fontSize = 24.sp))),
                MenuContainer.SingleItem(MenuElement.TextItem("Center Line 2"))
            )
        ),
        rightContent = MenuContainer.ColumnContainer(
            listOf(
               // MenuContainer.SingleItem(MenuElement.IconItem(Icons.Default.Home, "Home")),
                //MenuContainer.SingleItem(MenuElement.ButtonItem("Click Me") { println("Button clicked!") }),
                MenuContainer.SingleItem(MenuElement.TextItem("Droite", isClickable = true)),
                MenuContainer.SingleItem(MenuElement.TextItem(nomApp))
            )
        )
    )

   // var idUser: Int = 0
    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SudokuWaveTheme {
                Scaffold(modifier = Modifier.fillMaxSize()
                ) { innerPadding ->

                    Column(modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color.Gray)
                        .padding(innerPadding))
                    {
                        // Afficher le menu en haut du contenu
                        val context = LocalContext.current
                        CustomMenu(
                            config = menuConfig,
                            onElementClick = { element -> handleMenuClick(element, context) }
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
    private fun handleMenuClick(element: MenuElement, context:Context) {
        when (element) {
            is MenuElement.TextItem -> {
                Log.d("Menu Click", "Text clicked: ${element.text}")
                Toast.makeText(context, "Clicked on: $element", Toast.LENGTH_LONG).show()
            }
            is MenuElement.IconItem -> Log.d("Menu Click", "Icon clicked: ${element.contentDescription}")
            is MenuElement.ButtonItem -> element.onClick.invoke()
            else -> Log.d("Menu Click", "Unknown element clicked")
        }
    }
    /***************************************************/
}



