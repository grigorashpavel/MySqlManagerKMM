package presentation.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import javax.swing.text.html.parser.Entity

@Composable
fun EntityDropdownMenu(
    state: Boolean,
    dismissRequest: () -> Unit,
    editRequest: () -> Unit,
    removeRequest: () -> Unit
) {
    DropdownMenu(
        expanded = state,
        onDismissRequest = dismissRequest
    ) {
        DropdownMenuItem(
            onClick = {
                editRequest()
                dismissRequest()
            }
        ) {
            Icon(imageVector = Icons.Default.Edit, contentDescription = null)
            Spacer(modifier = Modifier.weight(1f))
            Text("Edit")
        }
        DropdownMenuItem(
            onClick = {
                removeRequest()
                dismissRequest()
            }
        ) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            Spacer(modifier = Modifier.weight(1f))
            Text("Remove")
        }
    }
}