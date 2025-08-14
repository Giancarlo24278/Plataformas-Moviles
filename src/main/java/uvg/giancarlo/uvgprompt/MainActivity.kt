package uvg.giancarlo.uvgprompt


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import uvg.giancarlo.uvgprompt.ui.theme.UvgPromptTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UvgPromptTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Screen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}
@Composable
fun Screen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .border(
                width = 8.dp,
                Color(0xFF2C7635)
            )
            .padding(8.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.uvg_logo),
            contentDescription = "Logo UVG",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer { alpha = 0.1f })
        Column(
            modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Universidad del Valle de Guatemala",
                modifier = Modifier.padding(16.dp),
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = (35.sp),
                    textAlign = TextAlign.Center,
                    baselineShift = BaselineShift(0.3f)
                )
            )
            Text(
                text = "Programación de plataformas móviles, Sección 30",
                modifier = Modifier.padding(16.dp),
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    fontSize = (25.sp),
                    baselineShift = BaselineShift(0.5f)
                ))
            Row(modifier = modifier.fillMaxWidth()
                ,horizontalArrangement = Arrangement.SpaceBetween)

            {
                Text(
                    text = "Integrante",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = (20.sp),
                    ))
                Text(
                    text = "Giancarlo Sagastume",
                    ) }
            Row(modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween)
            {
                Text(
                    text = "Catedratico",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = (20.sp)
                 ))
                Text(text = " Juan Carlos Durini") }
            Text(
                text = "Giancarlo Sagastume ",
            modifier = modifier.padding(20.dp))

            Text(text = "24278",)
        }
    }
    }

