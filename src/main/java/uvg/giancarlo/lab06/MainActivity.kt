package uvg.giancarlo.lab06

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import android.R.attr.label
import android.R.attr.value
import android.os.Bundle
import android.webkit.WebSettings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import uvg.giancarlo.lab06.ui.theme.Lab06Theme
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                ContadorApp(modifier = Modifier.padding(innerPadding))
            }
        }
    }
}
@Composable
fun ContadorApp(modifier: Modifier = Modifier) {
    var count by remember { mutableStateOf(0) }
    val historial = remember { mutableStateListOf<Int>() }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Giancarlo Sagastume",
            style = TextStyle(
                fontSize = 38.sp,
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                count--
                historial.add(-1)
            }) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "Disminuir"
                )
            }
            Text(
                text = "$count",
                style = TextStyle(
                    fontSize = 80.sp,
                    fontWeight = FontWeight.Bold))
            Spacer(modifier = Modifier.height(24.dp))

            Spacer(modifier = Modifier.width(32.dp))
            IconButton(onClick = {
                count++
                historial.add(1)
            }) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "Aumentar"
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        val totalIncrementos = historial.count { it > 0 }
        val totalDecrementos = historial.count { it < 0 }
        val valorMaximo = (listOf(0) + historial.scan(0) { acc, v -> acc + v }).maxOrNull() ?: 0
        val valorMinimo = (listOf(0) + historial.scan(0) { acc, v -> acc + v }).minOrNull() ?: 0
        val totalCambios = historial.size

        Text("Incrementos totales: $totalIncrementos",
            style = TextStyle(
            fontSize = 20.sp))
        Text("Total decrementos: $totalDecrementos",
            style = TextStyle(
                fontSize = 20.sp))
        Text("Valor maximo: $valorMaximo",
            style = TextStyle(
                fontSize = 20.sp))
        Text("Valor minimo: $valorMinimo",
            style = TextStyle(
                fontSize = 20.sp))
        Text("Total cambios: $totalCambios",
            style = TextStyle(
                fontSize = 20.sp))

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Historial:",
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(historial) { accion ->
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(
                            color = if (accion > 0) Color(0xFF4CAF50) else Color(0xFFF44336),
                            shape = RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (accion > 0) "+1" else "-1",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

