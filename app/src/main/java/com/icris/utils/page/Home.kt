package com.icris.utils.page


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import com.icris.nav.Nav
import com.icris.nav.goto
import com.icris.utils.page.destinations.DirectionDestination
import com.icris.utils.page.destinations.HomePageDestination
import com.icris.utils.page.destinations.ResTestPageDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph

@Preview(showSystemUi = true)
@Destination
@RootNavGraph(start = true)
@Composable
fun HomePage() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        LazyColumn {
            items(routes) {
                RouterItem(it)
            }
        }
    }
}

@Composable
fun RouterItem(p: Pair<String, DirectionDestination>) {
    Button(onClick = { Nav goto p.second }) {
        Text(text = p.first)
    }
}

val routes = listOf(
    "Home" to HomePageDestination,
    "Res" to ResTestPageDestination,
)