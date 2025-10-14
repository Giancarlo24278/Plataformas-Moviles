package uvg.giancarlo.lab10.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uvg.giancarlo.lab10.datastore.UserPreferences
import uvg.giancarlo.lab10.repository.UserRepository

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository: UserRepository

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName.asStateFlow()

    init {
        val userPreferences = UserPreferences(application)
        userRepository = UserRepository(userPreferences)

        viewModelScope.launch {
            userRepository.userName.collect { name ->
                _userName.value = name ?: "Usuario"
            }
        }
    }

    fun logout(onSuccess: () -> Unit) {
        viewModelScope.launch {
            userRepository.logout()
            onSuccess()
        }
    }
}