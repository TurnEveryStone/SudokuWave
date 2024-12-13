// MainActivity.kt
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
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

class MainActivity : AppCompatActivity() {

    var nomApp: String = "Le Sudoku"
    var userNumber: String = "Nobody"

    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val snackbarHostState = remember { SnackbarHostState() }
            val coroutineScope = rememberCoroutineScope()

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
                        val context = LocalContext.current
                        CustomMenu(
                            config = currentMenuConfig.value, // Passe explicitement la configuration
                            onElementClick = { element: MenuElement ->
                                element.actionKey?.let { actionKey ->
                                    handleMenuAction(actionKey, supportFragmentManager) { newMenu ->
                                        currentMenuConfig.value = newMenu
                                    }
                                }
                            }
                            ,
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

    @Composable
    fun ObserveViewModel(sharedViewModel: SharedViewModel) {
        val value = sharedViewModel.stateFlowVariable.collectAsState().value
        Text(text = "La variable StateFlow a changé : $value")
    }

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

    /**
     * Handles actions triggered by menu interactions.
     *
     * @param actionKey A string representing the action to perform.
     * @param fragmentManager The FragmentManager used to manage fragment transactions.
     * @param updateMenu A function to update the current menu configuration.
     *
     * Actions can include:
     * - "EnableNotifications": Logs a message for enabling notifications.
     * - "ToggleDarkMode": Logs a message for toggling dark mode.
     * - "GoToSettings": Replaces the current fragment with a SettingsFragment and updates the menu.
     * - Default: Logs a warning for unknown action keys.
     */

    fun handleMenuAction(
        actionKey: String,
        fragmentManager: FragmentManager,
        updateMenu: (MenuConfig) -> Unit
    ) {
        when (actionKey) {
            "EnableNotifications" -> Log.d("MenuAction", "Notifications toggled")
            "ToggleDarkMode" -> Log.d("MenuAction", "Dark Mode toggled")
            "GoToSettings" -> {
                fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, SettingsFragment())
                    .addToBackStack(null)
                    .commit()

                updateMenu(getMenuConfig("Settings"))
            }
            else -> Log.w("MenuAction", "Unknown actionKey: $actionKey")
        }
    }





}
