package com.icris.utils.page


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.icris.res.Res
import com.icris.utils.utils.log
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.datetime.Clock
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds

@Preview(showSystemUi = true)
@Destination
@Composable
fun ResTestPage() {
    val resList = remember {
        List(100) { Res(1.seconds, true) {
         if (Random.nextBoolean()) null else   Clock.System.now().toString()
        } }
    }
    LaunchedEffect(Unit) {
        Res(1.seconds, true) { Clock.System.now().toString() }.asFlow().collect {
            it.log()
        }
    }
    Column(
        Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("ResTest page")
        LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
            items(resList.size) {
                Box(modifier = Modifier.height(48.dp).fillMaxWidth(), Alignment.Center) {
                    Text(text = resList[it].get() ?: "")
                }
            }
        }
    }
}

class ResTestViewModel : ViewModel() {

}
