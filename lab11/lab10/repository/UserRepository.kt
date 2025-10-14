package uvg.giancarlo.lab10.repository

import kotlinx.coroutines.flow.Flow
import uvg.giancarlo.lab10.datastore.UserPreferences

class UserRepository(private val userPreferences: UserPreferences) {

    val userName: Flow<String?> = userPreferences.userName

    suspend fun saveUserName(name: String) {
        userPreferences.saveUserName(name)
    }

    suspend fun logout() {
        userPreferences.clearUserName()
    }
}