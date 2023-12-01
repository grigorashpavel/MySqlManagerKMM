package presentation.navigation

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.NavigationRail
import androidx.compose.material.NavigationRailItem
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun RailNavigationMenu(
    modifier: Modifier = Modifier,
    tables: List<String>,
    currentTable: String,
    navigationClick: (String) -> Unit,
) {
    NavigationRail(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .fillMaxHeight()
            .width(128.dp)
    ) {
        tables.forEach { name ->
            NavigationRailItem(
                selected = currentTable == name,
                icon = {
                    Icon(
                        painter = painterResource("ic_data_table24.xml"),
                        contentDescription = null
                    )
                },
                label = {
                    Text(
                        name,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                onClick = { navigationClick(name) }
            )
        }

        FloatingActionButton(
            onClick = { navigationClick("FunctionsAndProcedures") },
            modifier = Modifier
                .padding(top = 16.dp)
        ) {
            Icon(imageVector = Icons.Default.Build, contentDescription = null)
        }
    }
}