package com.example.sudokuwave

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.activity.OnBackPressedCallback
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
import androidx.lifecycle.ViewModel
import com.example.sudokuwave.ui.CustomMenu
import com.example.sudokuwave.ui.MenuConfig
import com.example.sudokuwave.ui.getMenuConfig
import com.example.sudokuwave.ui.theme.SudokuWaveTheme
import viewmodel.SharedViewModel
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sudokuwave.tools.ThemeViewModel

/*****************************************/
class MainActivity : AppCompatActivity() {
    class MainActivityViewModel : ViewModel() {
        //var currentFragmentTag: String = "HomeFragment"
        var currentMenuConfig: MenuConfig = getMenuConfig("Home")
        private val menuStack = mutableListOf<MenuConfig>()
        val seletedMenuColor:Color=Color.Red
        val unseletedMenuColor:Color=Color.DarkGray

        fun pushMenu(menuConfig: MenuConfig) {

            menuStack.add(menuConfig)

        }

        fun popMenu(): MenuConfig? {
            return if (menuStack.isNotEmpty()) menuStack.removeAt(menuStack.size - 1) else null
        }
    }

    val viewModel: MainActivityViewModel by viewModels()
    val nomApp: String = "Le Sudoku"
  //  var userNumber: String = "Nobody"
   // private val containerId = View.generateViewId()
    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                handleCustomBackPressed()
            }
        })



        enableEdgeToEdge()



        if (savedInstanceState == null) {
            // Pas d'état sauvegardé, charger le fragment initial

            setContent {
            //    val themeViewModel: ThemeViewModel = viewModel()
                val snackbarHostState = remember { SnackbarHostState() }
                //val coroutineScope = rememberCoroutineScope()
           //     MyApp(themeViewModel)
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
                        val containerId = remember { View.generateViewId() }
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color = Color.Transparent)
                                .padding(innerPadding)
                        ) {
                            val fragmentManager = supportFragmentManager
                            //val fragmentManager = (LocalContext.current as AppCompatActivity).supportFragmentManager //
                            val handledActions = mutableSetOf<String>() // Ensemble des actions déjà traitées
                            CustomMenu(
                                config = currentMenuConfig.value,
                                onElementClick = { element, actionKey ->
                                    actionKey?.let { key ->
                                        if (!handledActions.contains(key)) { // Vérifier si l'action est déjà traitée
                                            handleMenuAction(
                                                key,
                                                containerId,
                                                fragmentManager
                                            ) { newMenu ->
                                                currentMenuConfig.value = newMenu
                                            }
                                            handledActions.add(key) // Marquer comme traité
                                        }
                                    }
                                },
                                onAction = { actionKey ->
                                    if (!handledActions.contains(actionKey)) { // Vérifier si l'action est déjà traitée
                                        handleMenuAction(
                                            actionKey,
                                            containerId,
                                            fragmentManager
                                        ) { newMenu ->
                                            currentMenuConfig.value = newMenu
                                        }
                                        actionKey.let { handledActions.add(it) } // Marquer comme traité
                                    }
                                }
                            )

// Réinitialiser l'ensemble à chaque cycle si nécessaire
                            handledActions.clear()



                            // Contenu principal
                            // Text(text = "Hello $userNumber")
                            FragmentContainerComposable(containerId)
                            ObserveViewModel(sharedViewModel)
                        }
                    }
                }
            }
        } else {
            // État sauvegardé : le fragment actuel est déjà restauré automatiquement par Android
            supportFragmentManager.executePendingTransactions()
        }
    }
   /*****************************************/
    @Composable
    fun FragmentContainerComposable(containerId: Int) {
        val fragmentManager = (LocalContext.current as AppCompatActivity).supportFragmentManager
        // containerId = remember { View.generateViewId() }
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
    fun handleMenuAction(
        actionKey: String,
        containerId: Int,
        fragmentManager: FragmentManager,
        updateMenu: (MenuConfig) -> Unit
    ) {
        val newMenuConfig = getMenuConfig(actionKey)



        // Vérifiez si le menu est déjà actif
        if (viewModel.currentMenuConfig == newMenuConfig) {
            return // Ne pas réagir si le menu est déjà actif
        }

        viewModel.pushMenu(viewModel.currentMenuConfig) // Sauvegarde le menu actuel
        viewModel.currentMenuConfig = newMenuConfig

        when (actionKey) {
            "Settings" -> replaceFragment(containerId, SettingsFragment(), fragmentManager,actionKey)
            "Profile" -> replaceFragment(containerId, ProfileFragment(), fragmentManager,actionKey)

            "Register" -> replaceFragment(containerId, RegisterFragment(), fragmentManager,actionKey)

            else -> Log.d("MenuAction", "Unknown action: $actionKey")
        }

        updateMenu(newMenuConfig)
    }
    /*****************************************/
   fun replaceFragment(containerId: Int, fragment: Fragment, fragmentManager: FragmentManager, tag: String)
    {
        val currentFragment = fragmentManager.findFragmentById(containerId)
        // Vérifiez si le fragment actuel est du même type que le nouveau fragment
        if (currentFragment != null && currentFragment::class == fragment::class) {
            // Si c'est le même type de fragment, ne faites rien pour éviter les doublons
            return
        }
        // Sinon, remplacez le fragment et ajoutez-le à la pile arrière
        fragmentManager.beginTransaction()
            .replace(containerId, fragment, tag)
            .addToBackStack(tag)
            .commit()
    }
    /****************************************/
    private fun handleCustomBackPressed() {
        val fragmentManager = supportFragmentManager

        if (fragmentManager.backStackEntryCount > 0) {
            fragmentManager.popBackStack() // Retour au fragment précédent
            val previousMenu = viewModel.popMenu() // Récupère le menu précédent
            if (previousMenu != null) {
                viewModel.currentMenuConfig = previousMenu
            }
        } else {
            finish() // Quitte l'activité si aucun fragment n'est disponible
        }
    }
    /****************************************/
    @Composable
    fun MyApp(themeViewModel: ThemeViewModel) {
        val selectedColor by themeViewModel.selectedMenuColor.observeAsState(R.color.default_selected_color)
        val unselectedColor by themeViewModel.unselectedMenuColor.observeAsState(R.color.default_unselected_color)

        MaterialTheme(
            colorScheme = MaterialTheme.colorScheme.copy(
                primary = Color(selectedColor),
                secondary = Color(unselectedColor)
            )
        ) {
            // Exemple de contenu
            Column {
                Text("Bonjour, Sudoku Wave !", color = MaterialTheme.colorScheme.primary)
            }
        }
    }
    /******************************************/
}
