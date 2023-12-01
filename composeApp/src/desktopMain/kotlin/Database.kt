import domain.model.ColumnItem
import domain.model.Entity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Types

object Database {
    private const val databaseName = "project"
    private val connection by lazy {
        DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/$databaseName",
            "root",
            "pasha"
        )
    }
    private val selectStatement get() = connection.createStatement()

    suspend fun getNameEntities(): List<String> = withContext(Dispatchers.IO) {
        val queryResults = selectStatement.executeQuery("show tables")

        val results = mutableListOf<String>()
        while (queryResults.next()) {
            results.add(queryResults.getString(1))
        }

        return@withContext results
    }

    suspend fun getEntity(entityName: String) = withContext(Dispatchers.IO) {
        val queryResult = selectStatement.executeQuery("select * from $entityName")
        val resultMetadata = queryResult.metaData

        val columns = List(resultMetadata.columnCount) { index ->
            return@List ColumnItem(
                name = resultMetadata.getColumnName(index + 1),
                type = resultMetadata.getColumnType(index + 1),
                isAutoIncrement = resultMetadata.isAutoIncrement(index + 1)
            )
        }

        val rows = mutableListOf<List<String>>()
        while (queryResult.next()) {
            rows.add(
                List(columns.size) { index -> queryResult.getValue(index + 1) }
            )
        }
        Entity(entityName, columns, rows)
    }

    private fun ResultSet.getValue(columnIndex: Int): String {
        return when (metaData.getColumnType(columnIndex)) {
            Types.VARCHAR -> getString(columnIndex)
            Types.INTEGER -> getInt(columnIndex)
            Types.TIMESTAMP -> getTimestamp(columnIndex).toString().removeSuffix(".0")
            Types.DOUBLE -> getDouble(columnIndex)
            else -> throw Exception("")
        }.toString()
    }

    suspend fun addEntry(
        entityName: String,
        columns: List<ColumnItem>,
        entry: List<String>
    ) {
        withContext(Dispatchers.IO) {
            println(generateAddQuery(entityName, columns, entry))
            selectStatement.executeUpdate(generateAddQuery(entityName, columns, entry))
        }
    }

    private fun generateAddQuery(
        entityName: String,
        columns: List<ColumnItem>,
        entry: List<String>
    ): String {
        var query = "insert $entityName("

        for (column in columns) {
            if (!column.isAutoIncrement)
                query += "${column.name}, "
        }
        query = query.removeSuffix(", ") + ")\n"

        query += "values ("
        for (i in columns.indices) {
            if (!columns[i].isAutoIncrement) {
                query += when (columns[i].type) {
                    Types.CHAR, Types.VARCHAR, Types.TIMESTAMP -> {
                        "\"${entry[i]}\", "
                    }

                    else -> "${entry[i]}, "
                }
            }
        }
        query = query.removeSuffix(", ") + ")"

        return "$query;"
    }

    suspend fun execQuery(query: String): String = withContext(Dispatchers.IO) {
        if ("CalculateOrderTotal" in query || "GetProductInfo" in query) {
            val result = selectStatement.executeQuery(query)

            var res = ""
            while (result.next()) {
                res = result.getString(1)
            }

            res
        } else {
            selectStatement.executeUpdate(query)
            ""
        }
    }

    suspend fun updateUniqueEntity(
        entityName: String,
        columns: List<ColumnItem>,
        oldEntry: List<String>,
        newEntry: List<String>
    ) = withContext(Dispatchers.IO) {
        selectStatement.executeUpdate(
            generateUpdateQuery(entityName, columns, oldEntry, newEntry)
        )
    }

    suspend fun removeUniqueEntity(
        entityName: String,
        columns: List<ColumnItem>,
        entry: List<String>
    ) {
        withContext(Dispatchers.IO) {
            selectStatement.executeUpdate(generateRemoveQuery(entityName, columns, entry))
        }
    }

    private fun generateRemoveQuery(
        entityName: String,
        columns: List<ColumnItem>,
        entry: List<String>
    ): String {
        var query = "delete from $entityName\n"

        query += "where "
        for (i in columns.indices) {
            if (i > 0) query += " and "
            query += "${columns[i].name} = "
            query += when (columns[i].type) {
                Types.CHAR, Types.VARCHAR, Types.TIMESTAMP -> {
                    "\"${entry[i]}\""
                }

                else -> entry[i]
            }
        }

        return "$query;"
    }

    private fun generateUpdateQuery(
        entityName: String,
        columns: List<ColumnItem>,
        oldEntry: List<String>,
        newEntry: List<String>
    ): String {
        var query = "update $entityName\n"

        query += "set "
        for (i in columns.indices) {
            if (!columns[i].isAutoIncrement) {
                query += "${columns[i].name} = "
                query += when (columns[i].type) {
                    Types.CHAR, Types.VARCHAR, Types.TIMESTAMP -> {
                        "\"${newEntry[i]}\", "
                    }

                    else -> "${newEntry[i]}, "
                }
            }
        }
        query = query.removeSuffix(", ") + "\n"

        query += "where "
        for (i in columns.indices) {
            if (i > 0) query += " and "
            query += "${columns[i].name} = "
            query += when (columns[i].type) {
                Types.CHAR, Types.VARCHAR, Types.TIMESTAMP -> {
                    "\"${oldEntry[i]}\""
                }

                else -> oldEntry[i]
            }
        }

        return "$query;"
    }
}