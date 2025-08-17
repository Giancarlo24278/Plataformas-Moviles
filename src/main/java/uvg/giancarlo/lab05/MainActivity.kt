package uvg.giancarlo.lab05


import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.ComponentActivity
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import uvg.giancarlo.lab05.ui.theme.Lab05Theme
import uvg.giancarlo.lab05.ui.theme.MapsActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab05Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PantallaPrincipal(
                        modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun PantallaPrincipal(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().background(Color(0xFFCAE0EF)),
            horizontalArrangement = Arrangement.SpaceBetween
        )
        {
            Icon(
                modifier = modifier
                    .size(30.dp),
                imageVector = Icons.Default.Settings,
                contentDescription = "Configuración",
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Actualización disponible",
                modifier = modifier,
                style = TextStyle(
                    fontSize = (20.sp)
                )
            )
            Button(
                onClick = {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=jp.pinbit.flygorilla")
                    )
                    context.startActivity(intent)
                },
                modifier = modifier,
                shape = RoundedCornerShape(0.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF328ED5)
                )
            )
            {
                Text(text = "Descargar")
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = modifier,
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                Text(
                    text = "Miércoles",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 50.sp,
                    )
                )
                Text(
                    text = "18 de Marzo",
                    style = TextStyle(
                        fontSize = 20.sp
                    )
                )
            }
            OutlinedButton(
                onClick = { },
                modifier = modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(0.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFF765AE5)
                )
            ) {
                Text(
                    text = "Terminar Jornada",
                    color = Color(0xFF765AE5)
                )
            }
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
            ) {
                Row(
                    modifier = modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween)
                {
                    Text(
                        text = "Toi Doi",
                        style = TextStyle(
                            fontSize = (30.sp),
                            fontWeight = FontWeight.Bold
                        )
                    )
                    IconButton(onClick = {
                        // Abrir la nueva pantalla
                        val intent = Intent(context, MapsActivity::class.java)
                        context.startActivity(intent)
                    }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Más opciones"
                        )
                    }}
                    Text(
                        text = "HGR5+P68, 19 Avenida, Cdad. de Guatemala 01015.",
                        style = TextStyle(
                            fontSize = (20.sp)
                        )
                    )
                    Text(
                        text = "8:00 - 10:00",
                        style = TextStyle(
                            fontSize = (17.sp)
                        )
                    )
                    Row(
                        modifier = modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        val context = LocalContext.current
                        Button(
                            onClick = {
                                val toast = Toast.makeText(
                                    context, "Giancarlo Sagastume", Toast.LENGTH_LONG
                                )
                                toast.show()
                                // Cancelar el toast exactamente después de 3 segundos
                                Handler(Looper.getMainLooper()).postDelayed({
                                    toast.cancel()
                                }, 3000)
                            },
                            modifier = modifier.height(60.dp),
                            shape = RoundedCornerShape(0.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFD56332),
                                contentColor = Color.White
                            )
                        ) {
                            Text(text = "Iniciar")
                        }
                        Button(
                            onClick = {
                                val toast = Toast.makeText(
                                    context, "Comida Vietnamita/Thailandes" +
                                            "\n Precio: 70Q - 110Q", Toast.LENGTH_LONG
                                )
                                toast.show()
                                // Cancelar el toast exactamente después de 3 segundos
                                Handler(Looper.getMainLooper()).postDelayed({
                                    toast.cancel()
                                }, 3000)
                            },
                            modifier = modifier
                                .height(60.dp),
                            shape = RoundedCornerShape(0.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = Color(0xFFD56332)
                            )
                        )
                        {
                            Text(text = "Detalles")
                        }
                    }
                }
            }
        }
    }



