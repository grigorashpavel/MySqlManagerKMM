package presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Card
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import domain.ViewModel
import domain.model.Entity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private fun getFilteredEntries(
    filters: List<String>, entries: List<List<String>>
): List<List<String>> {
    return entries.filter { entry ->
        entry.zip(filters) { field, filter -> field.startsWith(filter) }.all { it }
    }
}

@Composable
fun TableComponent(
    modifier: Modifier = Modifier,
    scope: CoroutineScope = rememberCoroutineScope(),
    viewModel: ViewModel,
    entity: Entity,
    searchState: Boolean,
    expandSheet: () -> Unit,
) {
    Box(modifier = modifier.fillMaxSize()) {
        Card(
            elevation = 8.dp,
            modifier = Modifier.fillMaxSize().padding(64.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                val filters by remember(searchState) {
                    mutableStateOf(MutableList(entity.columns.size) { "" })
                }
                Text(
                    entity.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .align(Alignment.Start)
                )

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (searchState) {
                        entity.columns.onEachIndexed { index, column ->
                            var value by remember { mutableStateOf("") }
                            TextField(
                                value = value,
                                label = { Text(text = column.name, fontSize = 10.sp) },
                                onValueChange = { newValue ->
                                    value = newValue
                                    filters[index] = newValue
                                },
                                maxLines = 1,
                                modifier = Modifier.weight(1f).align(Alignment.CenterVertically)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                    } else {
                        entity.columns.onEach {
                            Text(
                                text = it.name,
                                textAlign = TextAlign.Center,
                                maxLines = 1,
                                modifier = Modifier.weight(1f).align(Alignment.CenterVertically)
                            )
                        }
                    }
                }

                filters.let {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(top = 8.dp, bottom = 16.dp)
                    ) {
                        itemsIndexed(getFilteredEntries(it, entity.entries)) { index, entry ->
                            var isExpanded by remember { mutableStateOf(false) }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { isExpanded = true }
                            ) {
                                entry.onEach {
                                    Text(
                                        text = it,
                                        textAlign = TextAlign.Center,
                                        maxLines = 1,
                                        modifier = Modifier
                                            .padding(vertical = 4.dp)
                                            .weight(1f)
                                            .align(Alignment.CenterVertically)
                                    )
                                }

                                EntityDropdownMenu(
                                    state = isExpanded,
                                    dismissRequest = { isExpanded = false },
                                    editRequest = {
                                        viewModel.setEditableData(
                                            EditorContentState(
                                                entityName = entity.name,
                                                columns = entity.columns,
                                                entryIndex = index,
                                                entry = entry,
                                                isEditing = true
                                            )
                                        )

                                        expandSheet()
                                    },
                                    removeRequest = {
                                        scope.launch {
                                            viewModel.removeEntry(
                                                entity.name,
                                                entity.columns,
                                                entry
                                            )
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = {
                viewModel.setEditableData(
                    EditorContentState(
                        entityName = entity.name,
                        columns = entity.columns,
                        entryIndex = entity.entries.size + 1,
                        isEditing = true
                    )
                )
                scope.launch {
                    expandSheet()
                }
            },
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.BottomEnd)
                .padding(36.dp)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
        }
    }
}