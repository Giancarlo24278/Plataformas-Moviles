package uvg.giancarlo.lab10.data

data class LocationData(
    val id: Int,
    val name: String,
    val type: String,
    val dimension: String
)

object LocationsDb {
    val locations = listOf(
        LocationData(1, "Earth (C-137)", "Planet", "Dimension C-137"),
        LocationData(2, "Abadango", "Cluster", "Unknown"),
        LocationData(3, "Citadel of Ricks", "Space station", "Unknown"),
        LocationData(4, "Worldender's lair", "Planet", "Unknown")
    )
}