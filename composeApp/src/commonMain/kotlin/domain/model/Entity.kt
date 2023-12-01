package domain.model

data class Entity(
    val name: String,
    val columns: List<ColumnItem>,
    val entries: List<List<String>>
)