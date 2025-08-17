package uvg.giancarlo.lab05.ui.theme

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

class MapsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OpenGoogleMaps()
        }
    }
}

@Composable
fun OpenGoogleMaps() {
    // Dirección que quieres abrir
    val address = "HGR5+P68, 19 Avenida, Cdad. de Guatemala 01015"

    // URL de Google Maps
    val uri = Uri.parse("geo:0,0?q=$address")

    // Composable simple con texto que al hacer clic abre Google Maps
    val context = LocalContext.current
    Text(
        text = "Abrir Google Maps",
        modifier = Modifier
            .padding(16.dp)
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, uri)
                intent.setPackage("com.google.android.apps.maps")
                context.startActivity(intent)
            }
    )
}
