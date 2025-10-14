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

sealed interface AuthState {
    data object Loading : AuthState
    data object Authenticated : AuthState
    data object NotAuthenticated : AuthState
}

class SplashViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository: UserRepository

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    init {
        val userPreferences = UserPreferences(application)
        userRepository = UserRepository(userPreferences)

        checkAuthStatus()
    }

    private fun checkAuthStatus() {
        viewModelScope.launch {
            userRepository.userName.collect { userName ->
                _authState.value = if (userName != null) {
                    AuthState.Authenticated
                } else {
                    AuthState.NotAuthenticated
                }
            }
        }
    }
}