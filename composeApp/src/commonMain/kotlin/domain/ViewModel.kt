package domain


import androidx.compose.runtime.State
import domain.model.ColumnItem
import domain.model.Entity
import presentation.components.EditorContentState
import presentation.components.ErrorState

abstract class ViewModel {
    abstract val editorContentState: EditorContentState
    abstract val tableState: Entity?
    abstract val errorState: State<ErrorState>

    abstract fun setEditableData(otherState: EditorContentState)
    abstract fun clearEditableData()

    abstract suspend fun getEntries(tableName: String)
    abstract suspend fun editEntry(tableName: String, entryIndex: Int, newEntry: List<String>)
    abstract suspend fun removeEntry(
        tableName: String, columns: List<ColumnItem>, entry: List<String>
    )

    abstract suspend fun addEntry(tableName: String, entry: List<String>)

    abstract suspend fun addEntity(entityName: String, columns: List<ColumnItem>)
    abstract suspend fun executeQuery(query: String)
}