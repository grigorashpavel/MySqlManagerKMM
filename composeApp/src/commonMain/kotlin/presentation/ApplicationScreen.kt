package presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.AlertDialog
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.DialogProperties
import domain.ViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import presentation.components.TableComponent
import presentation.navigation.NavController
import presentation.navigation.NavigationHost
import presentation.navigation.RailNavigationMenu
import presentation.navigation.composable
import kotlin.coroutines.coroutineContext


@Composable
fun ApplicationScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    tables: List<String>,
    currentTable: String,
    navigationClick: (String) -> Unit,
    viewModel: ViewModel,
    searchState: MutableState<Boolean>
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("DB Manager by Pavel") },
                actions = {
                    IconButton(onClick = { searchState.value = true }) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = null)
                    }
                },
                navigationIcon = {
                    if (searchState.value) {
                        IconButton(onClick = { searchState.value = false }) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = null)
                        }
                    } else {
                        IconButton(onClick = { navController.navigate("WelcomeScreen") }) {
                            Icon(imageVector = Icons.Default.Menu, contentDescription = null)
                        }
                    }
                }
            )
        }
    ) {
        Box(modifier.fillMaxSize()) {
            if (viewModel.errorState.value.isError) {
                AlertDialog(
                    onDismissRequest = {  },
                    title = { Text("Operation Error") },
                    text = { Text(viewModel.errorState.value.message) },
                    shape = MaterialTheme.shapes.medium,
                    buttons = {}
                )
            }

            if (tables.isEmpty()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                Row {
                    RailNavigationMenu(
                        tables = tables,
                        currentTable = currentTable,
                        navigationClick = navigationClick
                    )

                    NavigationHost(navController) {
                        composable("WelcomeScreen") {
                            StartScreen()
                        }

                        for (table in tables) {
                            composable(route = table) {
                                TableScreen(
                                    tableName = table,
                                    viewModel = viewModel,
                                    searchState = searchState.value
                                )
                            }
                        }

                        composable("FunctionsAndProcedures") {
                            FunctionsScreen(
                                viewModel = viewModel
                            )
                        }
                    }.build()
                }
            }
        }
    }
}