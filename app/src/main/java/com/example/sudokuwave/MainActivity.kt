package com.example.sudokuwave
import HomeFragment
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.sudokuwave.ui.theme.SudokuWaveTheme
import com.example.sudokuwave.viewmodel.SharedViewModel

class MainActivity : AppCompatActivity() {
    var userNumber: String = "Nobody"
    var idUser: Int = 0
    var idFrag: Int = 0
    private val sharedViewModel: SharedViewModel by viewModels()
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SudokuWaveTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = Color.Gray)
                            .padding(innerPadding)
                    ) {
                        AppContent("Android")
                        FragmentContainerComposable()

                    }
                }
            }
        }
    }
    /***************************************************/
    @Composable
    fun AppContent(name: String, modifier: Modifier = Modifier) {
        val configuration = LocalConfiguration.current
        val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        var couleurIconesMenu: Color=Color.Blue
        Row(
            modifier = Modifier
                .fillMaxWidth()
                //           .background(Color.Gray, shape = RoundedCornerShape(8.dp))
                .background(Color.White)
                .padding(6.dp,0.dp,6.dp,0.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icônes à gauche
            IconButton(onClick = { /* Action pour l'icône Home */ }) {
                Icon(Icons.Default.Home, contentDescription = stringResource(id = R.string.home_icone), tint = couleurIconesMenu)
            }
            IconButton(onClick = { /* Action pour l'icône Search */ }) {
                Icon(Icons.Default.Person, contentDescription = stringResource(id = R.string.user_icone),tint = couleurIconesMenu)
            }
            Spacer(modifier = Modifier.weight(1f)) // Espace flexible pour séparer les icônes
            Text(stringResource(id = R.string.app_name)+" kikou", color = Color.Black)
            Spacer(modifier = Modifier.weight(1f)) // Espace flexible pour séparer les icônes
            // Icônes à droite
            IconButton(onClick = { /* Action pour l'icône Notifications */ }) {
                Icon(Icons.Default.Notifications, contentDescription = stringResource(id = R.string.notifications_icone), tint = couleurIconesMenu)
            }
            IconButton(onClick = { /* Action pour l'icône Settings */ }) {
                Icon(Icons.Default.Settings, contentDescription = stringResource(id = R.string.configuration_icone), tint = couleurIconesMenu)
            }
        }
    }
    /***************************************************/
    @Preview(showBackground = true)
    @Composable
    fun AppContentPreview() {
        SudokuWaveTheme {
            AppContent("Android")
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
                        .replace(containerId, HomeFragment())
                        .commit()
                }
            }
        )
    }
}

