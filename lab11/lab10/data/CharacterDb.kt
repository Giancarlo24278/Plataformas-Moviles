package uvg.giancarlo.lab10.data

data class CharacterData(
    val id: Int,
    val name: String,
    val species: String,
    val status: String,
    val gender: String
)

object CharactersDb {
    val characters = listOf(
        CharacterData(1, "Rick Sanchez", "Human", "Alive", "Male"),
        CharacterData(2, "Morty Smith", "Human", "Alive", "Male"),
        CharacterData(3, "Summer Smith", "Human", "Alive", "Female"),
        CharacterData(4, "Beth Smith", "Human", "Alive", "Female"),
        CharacterData(5, "Jerry Smith", "Human", "Alive", "Male"),
        CharacterData(6, "Abadango Alien", "Alien", "Alive", "Unknown")
    )
}