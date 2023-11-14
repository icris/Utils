package com.icris.utils

import android.media.MediaRouter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.icris.nav.AppNavObserve
import com.icris.utils.page.NavGraphs
import com.icris.utils.ui.theme.UtilsTheme
import com.ramcosta.composedestinations.DestinationsNavHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UtilsTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Router()
                }
            }
        }
    }
}

@Composable
fun Router() {
    val navController = rememberNavController()
    AppNavObserve(navController)
    DestinationsNavHost(NavGraphs.root, navController = navController)
}
