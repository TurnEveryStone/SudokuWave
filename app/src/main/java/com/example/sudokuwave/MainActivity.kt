package com.example.sudokuwave

import android.content.Context
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.sudokuwave.ui.CustomMenu
import com.example.sudokuwave.ui.MenuConfig
import com.example.sudokuwave.ui.MenuElement
import com.example.sudokuwave.ui.getMenuConfig
import com.example.sudokuwave.ui.theme.SudokuWaveTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import viewmodel.SharedViewModel
/*****************************************/
class MainActivity : AppCompatActivity() {

    val nomApp: String = "Le Sudoku"
    var userNumber: String = "Nobody"

    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val snackbarHostState = remember { SnackbarHostState() }
            //val coroutineScope = rememberCoroutineScope()

            SudokuWaveTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
                ) { innerPadding ->

                    /**
                     * Initialize the current menu configuration.
                     * "Home" is used as the default configuration because it represents
                     * the main entry point of the application. This can be parameterized
                     * in the future to allow dynamic selection of the initial menu based
                     * on user preferences or app state.
                     */
                    val currentMenuConfig = remember { mutableStateOf(getMenuConfig("Home")) }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = Color.Gray)
                            .padding(innerPadding)
                    ) {

                        //val fragmentManager = (LocalContext.current as AppCompatActivity).supportFragmentManager //
                        CustomMenu(
                            config = currentMenuConfig.value,
                            onElementClick = { element, actionKey -> // Access actionKey here
                                actionKey?.let { actionKey ->
                                    handleMenuAction(actionKey, supportFragmentManager) { newMenu ->
                                        currentMenuConfig.value = newMenu
                                    }
                                }
                            },
                            onAction = { actionKey ->
                                handleMenuAction(actionKey, supportFragmentManager) { newMenu ->
                                    currentMenuConfig.value = newMenu
                                }
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
    /*****************************************/
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
                        .setReorderingAllowed(true) // Ensure state restoration compatibility
                        .replace(containerId, HomeFragment())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE) // Smooth transitions
                        .commit()
                }
            }
        )
    }
/*****************************************/
    @Composable
    fun ObserveViewModel(sharedViewModel: SharedViewModel) {
        val value = sharedViewModel.stateFlowVariable.collectAsState().value
        Text(text = "La variable StateFlow a changé : $value")
    }
/*****************************************/
    private fun handleMenuClick(
        element: MenuElement,
        context: Context,
        snackbarHostState: SnackbarHostState,
        coroutineScope: CoroutineScope
    ) {
        when (element) {
            is MenuElement.TextItem -> {
                Log.d("Menu Click", "Text clicked: ${element.text}")
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Clicked: ${element.text}")
                }
            }

            is MenuElement.IconItem -> {
                Log.d("Menu Click", "Icon clicked: ${element.contentDescription}")
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Icon clicked: ${element.contentDescription}")
                }
            }

            is MenuElement.ButtonItem -> {
                element.onClick.invoke()
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Button clicked: ${element.text}")
                }
            }

            else -> {
                Log.d("Menu Click", "Unknown element clicked: ${element::class.simpleName}")
            }
        }
    }
/*****************************************/
fun handleMenuAction(actionKey: String, containerId: Int, fragmentManager: FragmentManager) {
    when (actionKey) {
        "GoToSettings" -> replaceFragment(containerId, SettingsFragment(), fragmentManager)
        "GoToProfile" -> replaceFragment(containerId, ProfileFragment(), fragmentManager)
        else -> Log.d("MenuAction", "Unknown action: $actionKey")
    }
}


/*****************************************/
    fun replaceFragment(containerId: Int, fragment: Fragment, fragmentManager: FragmentManager) {
        fragmentManager.beginTransaction()
            .replace(containerId, fragment)
            .addToBackStack(null)
            .commit()
    }
/****************************************/

}
