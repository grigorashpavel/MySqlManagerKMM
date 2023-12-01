package presentation

import Database
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.window.ApplicationScope
import domain.ViewModel
import domain.model.ColumnItem
import domain.model.Entity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import presentation.components.EditorContentState
import presentation.components.ErrorState

class EntityViewModel : ViewModel() {
    private val _editorStateHolder: MutableState<EditorContentState> =
        mutableStateOf(EditorContentState())
    private val _tableStateHolder: MutableState<Entity?> =
        mutableStateOf(null)

    override val editorContentState: EditorContentState
        get() = _editorStateHolder.value
    override val tableState: Entity?
        get() = _tableStateHolder.value

    private val _errorState = mutableStateOf<ErrorState>(ErrorState())
    override val errorState: State<ErrorState> = _errorState

    private fun restoreErrorState() {
        _errorState.value = ErrorState()
    }

    private fun setErrorState(e: Exception) {
        _errorState.value = ErrorState(isError = true, message = e.message ?: "")
    }

    private fun setErrorState(msg: String) {
        _errorState.value = ErrorState(isError = true, message = msg)
    }

    override fun setEditableData(otherState: EditorContentState) {
        _editorStateHolder.value = otherState
    }

    override fun clearEditableData() {
        _editorStateHolder.value = EditorContentState()
    }

    override suspend fun getEntries(tableName: String) {
        try {
            _tableStateHolder.value = Database.getEntity(tableName)
        } catch (e: Exception) {
            setErrorState(e)
            delay(2500)
            restoreErrorState()
        }
    }


    override suspend fun editEntry(tableName: String, entryIndex: Int, newEntry: List<String>) {
        _tableStateHolder.value?.entries?.let {
            val columns = tableState!!.columns

            if (it.size >= entryIndex + 1 && columns.isNotEmpty()) {
                val oldEntry = it[entryIndex]

                try {
                    Database.updateUniqueEntity(tableName, columns, oldEntry, newEntry)
                    getEntries(tableName)
                } catch (e: Exception) {
                    setErrorState(e)
                    delay(2500)
                    restoreErrorState()
                }
            }
        }
    }

    override suspend fun removeEntry(
        tableName: String,
        columns: List<ColumnItem>,
        entry: List<String>
    ) {
        try {
            Database.removeUniqueEntity(tableName, columns, entry)
            getEntries(tableName)
        } catch (e: Exception) {
            setErrorState(e)
            delay(2500)
            restoreErrorState()
        }
    }

    override suspend fun addEntry(
        tableName: String,
        entry: List<String>
    ) {
        val columns = tableState!!.columns

        try {
            Database.addEntry(tableName, columns, entry)
            getEntries(tableName)
        } catch (e: Exception) {
            setErrorState(e)
            delay(2500)
            restoreErrorState()
        }
    }


    override suspend fun addEntity(entityName: String, columns: List<ColumnItem>) {
        TODO("Not yet implemented")
    }

    override suspend fun executeQuery(query: String) {
        try {
            val result = Database.execQuery(query)
            if (result.isNotEmpty()) {
                setErrorState(result)
                delay(2500)
                restoreErrorState()
            }
        } catch (e: Exception) {
            setErrorState(e)
            delay(2500)
            restoreErrorState()
        }
    }
}