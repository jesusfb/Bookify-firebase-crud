package ua.yahniukov.bookify.presentation.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ua.yahniukov.bookify.domain.usecases.auth.AuthUseCase
import ua.yahniukov.bookify.domain.usecases.user.UserUseCase
import ua.yahniukov.bookify.utils.Result
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    userUseCase: UserUseCase
) : ViewModel() {
    private var _uiState = MutableLiveData<Result<Nothing>>(Result.Idle)
    val uiState: LiveData<Result<Nothing>> = _uiState

    init {
        if (userUseCase.getCurrentUserUseCase() != null) {
            _uiState.value = Result.Success()
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = Result.Loading
            _uiState.value = authUseCase.logInUseCase(email, password)
        }
    }
}