package com.example.sudokuwave

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.viewinterop.AndroidView
import com.example.sudokuwave.ui.CustomMenu
import com.example.sudokuwave.ui.MenuElement
import com.example.sudokuwave.ui.theme.SudokuWaveTheme
import viewmodel.SharedViewModel

/***************************************************/
class MainActivity : AppCompatActivity() {
    var userNumber: String = "Nobody"
    var idUser: Int = 0
    private val sharedViewModel: SharedViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val currentFragmentTag = remember { sharedViewModel.stateFlowVariable }
            SudokuWaveTheme {
                Scaffold(modifier = Modifier.fillMaxSize(),
                    topBar = {
                        val (leftContent, centerContent, rightContent) = getMenuConfigForFragment(currentFragmentTag.value)
                        CustomMenu(
                            leftContent = leftContent,
                            centerContent = centerContent,
                            rightContent = rightContent,
                            onElementClick = { element ->
                                println("Clicked on $element")
                            }
                        )
                    }
                ) { innerPadding ->

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = Color.Gray)
                            .padding(innerPadding)
                    ) {
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
    private fun getMenuConfigForFragment(fragmentTag: String): Triple<List<MenuElement>, List<MenuElement>, List<MenuElement>> {
        return when (fragmentTag) {
            "HomeFragment" -> Triple(
                listOf(MenuElement.TextItem("Home", isClickable = true)),
                listOf(MenuElement.TextItem("MyApp", isClickable = false)),
                listOf(MenuElement.ButtonItem("Click Me") { println("Button clicked!") })
            )
            else -> Triple(emptyList(), emptyList(), emptyList())
        }
    }
    /***************************************************/
}



