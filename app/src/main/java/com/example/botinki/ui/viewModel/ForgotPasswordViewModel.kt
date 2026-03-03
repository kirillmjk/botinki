package com.example.tyagi_shop.ui.viewModel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tyagi_shop.data.RetrofitInstance
import kotlinx.coroutines.launch

class ForgotPasswordViewModel : ViewModel() {
    val showDialog = mutableStateOf(false)
    val errorMessage = mutableStateOf<String?>(null)

    fun sendRecoveryEmail(email: String) {
        viewModelScope.launch {
            try {
                errorMessage.value = null

                val response = RetrofitInstance.userManagementService
                    .recoverPassword(mapOf("email" to email))

                Log.d("ForgotPassword", "Результат запроса: ${response.isSuccessful} ${response.code()} ${response.message()}")
                if (response.isSuccessful) {
                    showDialog.value = true
                } else {
                    when (response.code()) {
                        409 -> errorMessage.value = "Пользователь уже существует"
                        400 -> errorMessage.value = "Неверный формат email или пароля"
                        429 -> errorMessage.value = "Слишком много запросов, повторите позднее"
                        else -> errorMessage.value = "Ошибка сервера: ${response.code()} ${response.message()}"
                    }
                }
            } catch (e: Exception) {
                errorMessage.value = "Ошибка сети: ${e.message}"
            }
        }
    }
}
