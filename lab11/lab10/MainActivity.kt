package uvg.giancarlo.lab10

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import uvg.giancarlo.lab10.navigation.Screen
import uvg.giancarlo.lab10.ui.theme.Lab10Theme
import uvg.giancarlo.lab10.viewmodel.*
import kotlin.system.exitProcess

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab10Theme {
                val navController = rememberNavController()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        val currentRoute = navController.currentBackStackEntry?.destination?.route
                        val shouldShowBottomBar = currentRoute != null &&
                                !currentRoute.contains("Splash") &&
                                !currentRoute.contains("Login")

                        if (shouldShowBottomBar) {
                            BottomNavigationBar(
                                currentRoute = currentRoute ?: "",
                                onNavigate = { destination ->
                                    when (destination) {
                                        "characters" -> navController.navigate(Screen.Characters) {
                                            popUpTo(Screen.Characters) { inclusive = true }
                                        }
                                        "locations" -> navController.navigate(Screen.Locations) {
                                            popUpTo(Screen.Locations) { inclusive = true }
                                        }
                                        "profile" -> navController.navigate(Screen.Profile) {
                                            popUpTo(Screen.Profile) { inclusive = true }
                                        }
                                    }
                                }
                            )
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Splash,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable<Screen.Splash> {
                            SplashScreen(
                                onAuthenticated = {
                                    navController.navigate(Screen.Characters) {
                                        popUpTo(Screen.Splash) { inclusive = true }
                                    }
                                },
                                onNotAuthenticated = {
                                    navController.navigate(Screen.Login) {
                                        popUpTo(Screen.Splash) { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable<Screen.Login> {
                            LoginScreen(
                                onLoginSuccess = {
                                    navController.navigate(Screen.Characters) {
                                        popUpTo(Screen.Login) { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable<Screen.Characters> {
                            BackHandler {
                                exitProcess(0)
                            }

                            val viewModel: CharactersViewModel = viewModel()
                            val state by viewModel.charactersState.collectAsState()

                            CharactersScreen(
                                state = state,
                                onRetry = { viewModel.loadCharacters() },
                                onPersonaClick = { persona ->
                                    navController.navigate(Screen.CharacterProfile(persona))
                                }
                            )
                        }

                        composable<Screen.CharacterProfile> { backStackEntry ->
                            val args = backStackEntry.toRoute<Screen.CharacterProfile>()
                            val viewModel: CharacterProfileViewModel = viewModel()
                            val state by viewModel.profileState.collectAsState()

                            LaunchedEffect(args.characterName) {
                                viewModel.loadCharacterProfile(args.characterName)
                            }

                            CharacterProfileScreen(
                                state = state,
                                onRetry = { viewModel.loadCharacterProfile(args.characterName) },
                                onBackToPersonas = { navController.navigateUp() }
                            )
                        }

                        composable<Screen.Locations> {
                            val viewModel: LocationsViewModel = viewModel()
                            val state by viewModel.locationsState.collectAsState()

                            LocationsScreen(
                                state = state,
                                onRetry = { viewModel.loadLocations() },
                                onLocationClick = { locationId ->
                                    navController.navigate(Screen.LocationProfile(locationId))
                                }
                            )
                        }

                        composable<Screen.LocationProfile> { backStackEntry ->
                            val args = backStackEntry.toRoute<Screen.LocationProfile>()
                            val viewModel: LocationProfileViewModel = viewModel()
                            val state by viewModel.locationState.collectAsState()

                            LaunchedEffect(args.locationId) {
                                viewModel.loadLocationProfile(args.locationId)
                            }

                            LocationProfileScreen(
                                state = state,
                                onRetry = { viewModel.loadLocationProfile(args.locationId) },
                                onBackToLocations = { navController.navigateUp() }
                            )
                        }

                        composable<Screen.Profile> {
                            UserProfile(
                                onLogout = {
                                    navController.navigate(Screen.Login) {
                                        popUpTo(0) { inclusive = true }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    ) {
        NavigationBarItem(
            selected = currentRoute.contains("Characters") ||
                    currentRoute.contains("CharacterProfile"),
            onClick = { onNavigate("characters") },
            icon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Characters"
                )
            },
            label = { Text("Characters") }
        )

        NavigationBarItem(
            selected = currentRoute.contains("Location"),
            onClick = { onNavigate("locations") },
            icon = {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Locations"
                )
            },
            label = { Text("Locations") }
        )

        NavigationBarItem(
            selected = currentRoute.contains("Profile") &&
                    !currentRoute.contains("Character") &&
                    !currentRoute.contains("Location"),
            onClick = { onNavigate("profile") },
            icon = {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Profile"
                )
            },
            label = { Text("Profile") }
        )
    }
}

// ============================================
// PANTALLAS CON ESTADOS
// ============================================

@Composable
fun CharactersScreen(
    state: UiState<List<String>>,
    onRetry: () -> Unit,
    onPersonaClick: (String) -> Unit
) {
    when (state) {
        is UiState.Loading -> LoadingScreen()
        is UiState.Error -> ErrorScreen(message = state.message, onRetry = onRetry)
        is UiState.Success -> PersonasList(
            personas = state.data,
            onPersonaClick = onPersonaClick
        )
    }
}

@Composable
fun CharacterProfileScreen(
    state: UiState<CharacterDetails>,
    onRetry: () -> Unit,
    onBackToPersonas: () -> Unit
) {
    when (state) {
        is UiState.Loading -> LoadingScreen()
        is UiState.Error -> ErrorScreen(message = state.message, onRetry = onRetry)
        is UiState.Success -> Perfil(
            character = state.data,
            onBackToPersonas = onBackToPersonas
        )
    }
}

@Composable
fun LocationsScreen(
    state: UiState<List<LocationInfo>>,
    onRetry: () -> Unit,
    onLocationClick: (String) -> Unit
) {
    when (state) {
        is UiState.Loading -> LoadingScreen()
        is UiState.Error -> ErrorScreen(message = state.message, onRetry = onRetry)
        is UiState.Success -> LocationsList(
            locations = state.data,
            onLocationClick = onLocationClick
        )
    }
}

@Composable
fun LocationProfileScreen(
    state: UiState<LocationInfo>,
    onRetry: () -> Unit,
    onBackToLocations: () -> Unit
) {
    when (state) {
        is UiState.Loading -> LoadingScreen()
        is UiState.Error -> ErrorScreen(message = state.message, onRetry = onRetry)
        is UiState.Success -> Ubicacion(
            location = state.data,
            onBackToLocations = onBackToLocations
        )
    }
}

// ============================================
// LOADING Y ERROR SCREENS
// ============================================

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp),
            color = Color(0xFF765AE5)
        )
    }
}

@Composable
fun ErrorScreen(message: String, onRetry: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Error",
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.error
            )

            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            )

            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF765AE5)
                )
            ) {
                Text("Reintentar")
            }
        }
    }
}

// ============================================
// LOGIN SCREEN
// ============================================

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = viewModel(),
    onLoginSuccess: () -> Unit
) {
    var userName by remember { mutableStateOf("") }
    val loginState by viewModel.loginState.collectAsState()

    LaunchedEffect(loginState) {
        if (loginState is LoginState.Success) {
            onLoginSuccess()
            viewModel.resetLoginState()
        }
    }

    BackHandler {
        exitProcess(0)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Icono de RyM
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "RyM Logo",
            tint = Color(0xFF765AE5),
            modifier = Modifier.size(120.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Rick and Morty",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Ingresa tu nombre para registrarte",
            fontSize = 16.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Input para el nombre
        OutlinedTextField(
            value = userName,
            onValueChange = { userName = it },
            label = { Text("Nombre de usuario") },
            modifier = Modifier.fillMaxWidth(),
            enabled = loginState !is LoginState.Loading,
            singleLine = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Botón Registrarse
        Button(
            onClick = { viewModel.login(userName) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = loginState !is LoginState.Loading,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF765AE5)
            )
        ) {
            if (loginState is LoginState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Sincronizando datos...")
            } else {
                Text("Registrarse", fontSize = 16.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Giancarlo Sagastume 24278",
            fontSize = 14.sp,
            color = Color.Gray
        )
    }
}


// ============================================
// SPLASH SCREEN
// ============================================

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    viewModel: SplashViewModel = viewModel(),
    onAuthenticated: () -> Unit,
    onNotAuthenticated: () -> Unit
) {
    val authState by viewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authenticated -> onAuthenticated()
            is AuthState.NotAuthenticated -> onNotAuthenticated()
            is AuthState.Loading -> { /* Mostrar loading */ }
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "Logo",
            tint = Color(0xFF765AE5),
            modifier = Modifier.size(120.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        CircularProgressIndicator(
            color = Color(0xFF765AE5)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Cargando...",
            fontSize = 16.sp,
            color = Color.Gray
        )
    }
}

// ============================================
// UI COMPONENTS
// ============================================

@Composable
fun PersonasList(
    modifier: Modifier = Modifier,
    personas: List<String>,
    onPersonaClick: (String) -> Unit
) {
    Column(modifier = modifier) {
        Text(
            text = "Characters",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(personas) { persona ->
                PersonaItem(
                    name = persona,
                    onPersonaClick = { onPersonaClick(persona) }
                )
            }
        }
    }
}

@Composable
fun PersonaItem(
    name: String,
    onPersonaClick: () -> Unit = {}
) {
    val status = if (name.contains("Alien", ignoreCase = true)) "Alien" else "Humano"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onPersonaClick() },
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            imageVector = getIconForPersona(name),
            contentDescription = "Character icon",
            tint = Color(0xFF765AE5),
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color(0xFFE0E0E0))
                .padding(8.dp)
        )

        Column {
            Text(text = name, style = MaterialTheme.typography.titleMedium)
            Text(text = status, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun LocationsList(
    modifier: Modifier = Modifier,
    locations: List<LocationInfo>,
    onLocationClick: (String) -> Unit
) {
    Column(modifier = modifier) {
        Text(
            text = "Locations",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(locations) { location ->
                LocationItem(
                    location = location,
                    onLocationClick = { onLocationClick(location.id) }
                )
            }
        }
    }
}

@Composable
fun LocationItem(
    location: LocationInfo,
    onLocationClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onLocationClick() },
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = "Location",
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color(0xFFE0E0E0))
                .padding(8.dp),
            tint = Color(0xFF765AE5)
        )

        Column {
            Text(text = location.name, style = MaterialTheme.typography.titleMedium)
            Text(text = location.type, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun Perfil(
    modifier: Modifier = Modifier,
    character: CharacterDetails,
    onBackToPersonas: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(onClick = { onBackToPersonas() }) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "Regresar"
                )
            }
            Text(
                text = "Contactos",
                modifier = Modifier.padding(top = 12.dp),
                style = TextStyle(fontSize = 16.sp)
            )
        }

        Icon(
            imageVector = getIconForPersona(character.name),
            contentDescription = "Character Icon",
            tint = Color(0xFF765AE5),
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color(0xFFE0E0E0))
                .padding(16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = character.name,
            style = MaterialTheme.typography.titleMedium,
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DetailRow(label = "Species:", value = character.species)
            DetailRow(label = "Status:", value = character.status)
            DetailRow(label = "Gender:", value = character.gender)
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun Ubicacion(
    modifier: Modifier = Modifier,
    location: LocationInfo,
    onBackToLocations: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(onClick = { onBackToLocations() }) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "Regresar"
                )
            }
            Text(
                text = "Location Details",
                modifier = Modifier.padding(top = 12.dp),
                style = TextStyle(fontSize = 18.sp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = location.name,
            style = MaterialTheme.typography.titleLarge,
            fontSize = 24.sp
        )

        Text(
            text = location.type,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(32.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DetailRow(label = "ID:", value = location.id)
            DetailRow(label = "Type:", value = location.type)
            DetailRow(label = "Dimension:", value = location.dimension)
        }
    }
}

@Composable
fun UserProfile(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = viewModel(),
    onLogout: () -> Unit
) {
    val userName by viewModel.userName.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color(0xFFE0E0E0)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "User Profile",
                modifier = Modifier.size(80.dp),
                tint = Color(0xFF666666)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(0.8f),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Nombre:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                Text(
                    text = userName,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(0.8f),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Carné:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                Text(
                    text = "24278",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        OutlinedButton(
            onClick = {
                viewModel.logout {
                    onLogout()
                }
            },
            modifier = Modifier.width(200.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color.Gray
            )
        ) {
            Text("Cerrar sesión")
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

// ============================================
// FUNCIONES AUXILIARES
// ============================================

fun getIconForPersona(name: String): ImageVector {
    return when {
        name.contains("Rick", ignoreCase = true) -> Icons.Default.Face
        name.contains("Summer", ignoreCase = true) -> Icons.Default.Person
        name.contains("Beth", ignoreCase = true) -> Icons.Default.Favorite
        name.contains("Jerry", ignoreCase = true) -> Icons.Default.Person
        name.contains("Morty", ignoreCase = true) -> Icons.Default.Face
        name.contains("Alien", ignoreCase = true) -> Icons.Default.Star
        else -> Icons.Default.AccountCircle
    }
}