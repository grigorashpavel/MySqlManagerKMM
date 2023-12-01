package presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetEditor(
    modifier: Modifier = Modifier,
    sheetState: ModalBottomSheetState,
    editorContentState: EditorContentState,
    closeRequest: () -> Unit,
    doneRequest: (Int, List<String>) -> Unit,
) {
    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            EditorContent(
                content = editorContentState,
                closeRequest = closeRequest,
                doneRequest = doneRequest
            )
        },
        sheetElevation = 8.dp,
        sheetShape = MaterialTheme.shapes.medium,
        modifier = modifier.fillMaxWidth()
    ) {}
}

@Composable
private fun EditorContent(
    content: EditorContentState,
    closeRequest: () -> Unit,
    doneRequest: (Int, List<String>) -> Unit
) {
    if (content.isEditing) {
        val mutableData = if (content.entry.isEmpty()) {
            mutableListOf<String>().also {
                for (column in content.columns) {
                    if (column.isAutoIncrement) it.add(content.entryIndex.toString())
                    else it.add("")
                }
            }
        } else content.entry.toMutableList()

        val values by remember { mutableStateOf(mutableData) }

        Column(
            verticalArrangement = Arrangement.spacedBy(32.dp),
            modifier = Modifier.fillMaxWidth().padding(32.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = content.entityName)
                Spacer(modifier = Modifier.weight(1f))

                Button(onClick = { doneRequest(content.entryIndex, values) }) {
                    Icon(imageVector = Icons.Default.Done, contentDescription = null)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(onClick = closeRequest) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = null)
                }
            }
            values.onEachIndexed { index, it ->
                var field by remember { mutableStateOf(it) }
                TextField(
                    value = field,
                    label = { Text(text = content.columns[index].name) },
                    enabled = !content.columns[index].isAutoIncrement,
                    onValueChange = { newValue ->
                        field = newValue
                        values[index] = newValue
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    } else CircularProgressIndicator()
}