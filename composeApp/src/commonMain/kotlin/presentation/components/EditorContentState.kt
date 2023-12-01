package presentation.components

import domain.model.ColumnItem

data class EditorContentState(
    val entityName: String = "",
    val columns: List<ColumnItem> = emptyList(),
    val entry: List<String> = emptyList(),
    val entryIndex: Int = -1,
    val isEditing: Boolean = false
)