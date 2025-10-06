package uvg.giancarlo.lab10.navigation

import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable
    data object Inicio : Screen

    @Serializable
    data object Characters : Screen

    @Serializable
    data class CharacterProfile(val characterName: String) : Screen

    @Serializable
    data object Locations : Screen

    @Serializable
    data class LocationProfile(val locationId: String) : Screen

    @Serializable
    data object Profile : Screen
}

