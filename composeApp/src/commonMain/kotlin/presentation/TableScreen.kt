package presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import domain.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import presentation.components.BottomSheetEditor
import presentation.components.TableComponent

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TableScreen(
    modifier: Modifier = Modifier,
    scope: CoroutineScope = rememberCoroutineScope(),
    tableName: String,
    viewModel: ViewModel,
    searchState: Boolean
) {
    scope.launch { viewModel.getEntries(tableName) }

    val bottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )
    if (bottomSheetState.currentValue != ModalBottomSheetValue.Hidden) {
        DisposableEffect(Unit) {
            onDispose { viewModel.clearEditableData() }
        }
    }

    Box(modifier = modifier) {
        viewModel.tableState?.let {
            TableComponent(
                expandSheet = {
                    scope.launch {
                        bottomSheetState.show()
                    }
                },
                entity = it,
                searchState = searchState,
                viewModel = viewModel
            )

            BottomSheetEditor(
                sheetState = bottomSheetState,
                editorContentState = viewModel.editorContentState,
                closeRequest = {
                    scope.launch { bottomSheetState.hide() }
                },
                doneRequest = { rowIndex, newData ->
                    scope.launch {
                        viewModel.tableState?.entries?.size?.let { numEntries ->
                            if (rowIndex > numEntries) {
                                viewModel.addEntry(tableName, newData)
                            } else viewModel.editEntry(tableName, rowIndex, newData)
                        }

                        bottomSheetState.hide()
                        viewModel.clearEditableData()
                    }
                }
            )
        }
    }
}