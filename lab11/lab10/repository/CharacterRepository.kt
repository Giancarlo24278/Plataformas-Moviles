package uvg.giancarlo.lab10.repository

import kotlinx.coroutines.flow.Flow
import uvg.giancarlo.lab10.data.CharactersDb
import uvg.giancarlo.lab10.room.CharacterDao
import uvg.giancarlo.lab10.room.CharacterEntity

class CharacterRepository(private val characterDao: CharacterDao) {

    val allCharacters: Flow<List<CharacterEntity>> = characterDao.getAllCharacters()

    suspend fun getCharacterById(id: Int): CharacterEntity? {
        return characterDao.getCharacterById(id)
    }

    suspend fun syncCharacters() {
        val characters = CharactersDb.characters.map { character ->
            CharacterEntity(
                id = character.id,
                name = character.name,
                species = character.species,
                status = character.status,
                gender = character.gender
            )
        }
        characterDao.insertAllCharacters(characters)
    }

    suspend fun deleteAllCharacters() {
        characterDao.deleteAllCharacters()
    }
}