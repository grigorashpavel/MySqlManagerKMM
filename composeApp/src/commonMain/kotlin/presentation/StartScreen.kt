package presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun StartScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text("Created by", fontSize = 20.sp)
            Text("Григораш Павел - beworld", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("ИКБО-06-21", fontSize = 20.sp)

            Icon(
                painter = painterResource("compose-multiplatform.xml"),
                tint = Color.Unspecified,
                contentDescription = null
            )
            Text(
                "Compose Multiplatform",
                fontSize = 44.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xff083042)
            )
        }
    }
}