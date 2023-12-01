package presentation

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import domain.ViewModel
import kotlinx.coroutines.launch


@Composable
fun FunctionsScreen(
    modifier: Modifier = Modifier,
    viewModel: ViewModel
) {
    val scope = rememberCoroutineScope()
    Card(
        elevation = 8.dp,
        modifier = modifier.fillMaxSize().padding(64.dp)
    ) {
        Column(
            modifier = Modifier
                .scrollable(rememberScrollState(), orientation = Orientation.Vertical)
        ) {
            Text(
                text = "Functions", modifier = Modifier
                    .padding(vertical = 16.dp)
            )
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    "CalculateOrderTotal", modifier = Modifier
                        .weight(1f).align(Alignment.CenterVertically)
                )
                var orderId by remember {
                    mutableStateOf("")
                }
                TextField(
                    value = orderId,
                    label = { Text("order_id") },
                    onValueChange = { new -> orderId = new },
                    modifier = Modifier
                        .weight(4f).align(Alignment.CenterVertically)
                )
                Button(
                    onClick = {
                        scope.launch {
                            viewModel.executeQuery("select CalculateOrderTotal($orderId);")
                        }
                    }, modifier = Modifier
                        .weight(1f).align(Alignment.CenterVertically)
                ) {}
            }
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                var productName by remember {
                    mutableStateOf("")
                }
                Text(
                    "GetProductInfo", modifier = Modifier
                        .weight(1f).align(Alignment.CenterVertically)
                )
                TextField(
                    value = productName,
                    label = { Text("product_name") },
                    onValueChange = { new -> productName = new },
                    modifier = Modifier
                        .weight(4f).align(Alignment.CenterVertically)
                )
                Button(
                    onClick = {
                        scope.launch {
                            viewModel.executeQuery("select GetProductInfo(\"$productName\");")
                        }
                    }, modifier = Modifier
                        .weight(1f).align(Alignment.CenterVertically)
                ) {}
            }
            Divider(
                color = Color.Gray, thickness = 2.dp, modifier = Modifier
                    .padding(vertical = 16.dp)
            )

            // ---------------------------------------------------------------
            Text(
                text = "Procedures", modifier = Modifier
                    .padding(vertical = 16.dp)
            )

            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    "AddProduct", modifier = Modifier
                        .weight(1f).align(Alignment.CenterVertically)
                )
                var orderId by remember { mutableStateOf("") }
                TextField(
                    value = orderId,
                    label = { Text("other_id_order") },
                    onValueChange = { new -> orderId = new },
                    modifier = Modifier
                        .weight(2f).align(Alignment.CenterVertically)
                )
                var productId by remember { mutableStateOf("") }
                TextField(
                    value = productId,
                    label = { Text("other_id_product") },
                    onValueChange = { new -> productId = new },
                    modifier = Modifier
                        .weight(2f).align(Alignment.CenterVertically)
                )
                var employeeId by remember { mutableStateOf("") }
                TextField(
                    value = employeeId,
                    label = { Text("other_id_employee") },
                    onValueChange = { new -> employeeId = new },
                    modifier = Modifier
                        .weight(2f).align(Alignment.CenterVertically)
                )
                var quantity by remember { mutableStateOf("") }
                TextField(
                    value = quantity,
                    label = { Text("other_quantity") },
                    onValueChange = { new -> quantity = new },
                    modifier = Modifier
                        .weight(2f).align(Alignment.CenterVertically)
                )
                var discount by remember { mutableStateOf("") }
                TextField(
                    value = discount,
                    label = { Text("other_discount") },
                    onValueChange = { new -> discount = new },
                    modifier = Modifier
                        .weight(2f).align(Alignment.CenterVertically)
                )
                Button(
                    onClick = {
                        scope.launch {
                            viewModel.executeQuery(
                                """
                                    call AddProduct($orderId, $productId, $employeeId, $quantity, $discount);
                                """.trimIndent()
                            )
                        }
                    }, modifier = Modifier
                        .weight(1f).align(Alignment.CenterVertically)
                ) {}
            }
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    "UpdateProductInfo", modifier = Modifier
                        .weight(1f).align(Alignment.CenterVertically)
                )
                var productId by remember { mutableStateOf("") }
                TextField(
                    value = productId,
                    label = { Text("id_product") },
                    onValueChange = { new -> productId = new },
                    modifier = Modifier
                        .weight(2f).align(Alignment.CenterVertically)
                )
                var newName by remember { mutableStateOf("") }
                TextField(
                    value = newName,
                    label = { Text("new_name") },
                    onValueChange = { new -> newName = new },
                    modifier = Modifier
                        .weight(2f).align(Alignment.CenterVertically)
                )
                var newPrice by remember { mutableStateOf("") }
                TextField(
                    value = newPrice,
                    label = { Text("new_price") },
                    onValueChange = { new -> newPrice = new },
                    modifier = Modifier
                        .weight(2f).align(Alignment.CenterVertically)
                )
                Button(
                    onClick = {
                        scope.launch {
                            viewModel.executeQuery(
                                "call UpdateProductInfo($productId, \"$newName\", $newPrice);"
                            )
                        }
                    }, modifier = Modifier
                        .weight(1f).align(Alignment.CenterVertically)
                ) {}
            }
        }
    }
}