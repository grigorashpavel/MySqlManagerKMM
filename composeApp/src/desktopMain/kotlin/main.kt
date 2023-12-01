import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.coroutines.launch
import presentation.ApplicationScreen
import presentation.EntityViewModel
import presentation.navigation.*

fun main() = application {

    var tables by mutableStateOf(emptyList<String>())
    val navController by rememberNavController("WelcomeScreen")

    val searchState = mutableStateOf(false)

    rememberCoroutineScope().launch {
        tables = Database.getNameEntities().dropLast(1)
    }

    MaterialTheme {
        Window(onCloseRequest = ::exitApplication) {
            ApplicationScreen(
                tables = tables,
                navController = navController,
                currentTable = navController.currentScreen.value,
                navigationClick = { newTable ->
                    navController.currentScreen.value = newTable
                    navController.navigate(newTable)
                },
                viewModel = EntityViewModel(),
                searchState = searchState,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}