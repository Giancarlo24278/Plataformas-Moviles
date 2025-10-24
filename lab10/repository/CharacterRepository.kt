package uvg.giancarlo.lab10.repository

import kotlinx.coroutines.flow.Flow
import uvg.giancarlo.lab10.network.CharacterDto
import uvg.giancarlo.lab10.network.RetrofitInstance
import uvg.giancarlo.lab10.room.CharacterDao
import uvg.giancarlo.lab10.room.CharacterEntity

class CharacterRepository(private val characterDao: CharacterDao) {

    val allCharacters: Flow<List<CharacterEntity>> = characterDao.getAllCharacters()

    suspend fun getCharacterById(id: Int): CharacterEntity? {
        return characterDao.getCharacterById(id)
    }

    // Estrategia Offline First
    suspend fun syncCharactersFromApi(): Result<Unit> {
        return try {
            // Llamada al API
            val response = RetrofitInstance.api.getCharacters()

            // Mapear DTO a Entity
            val characters = response.results.map { dto ->
                CharacterEntity(
                    id = dto.id,
                    name = dto.name,
                    species = dto.species,
                    status = dto.status,
                    gender = dto.gender
                )
            }

            // Guardar en Room
            characterDao.insertAllCharacters(characters)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteAllCharacters() {
        characterDao.deleteAllCharacters()
    }
}