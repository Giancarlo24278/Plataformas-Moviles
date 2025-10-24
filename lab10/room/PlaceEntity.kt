package uvg.giancarlo.lab10.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "places")
data class PlaceEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val type: String,
    val dimension: String
)