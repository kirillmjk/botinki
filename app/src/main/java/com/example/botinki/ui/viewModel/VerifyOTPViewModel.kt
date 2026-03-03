package com.example.tyagi_shop.ui.viewModel

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.tyagi_shop.data.RetrofitInstance
import com.example.tyagi_shop.data.model.VerifyOtpRequest
import kotlinx.coroutines.launch

class VerifyOTPViewModel : ViewModel() {

    val errorMessage = mutableStateOf<String?>(null)
    // type: "signup" или "recovery"
    fun verifyOTP(
        email: String,
        token: String,
        type: String,
        context: Context,
        navController: NavController
    ) {
        viewModelScope.launch {
            try {
                val requestType = if (type == "recovery") "recovery" else "signup"

                val request = VerifyOtpRequest(
                    type = requestType,
                    email = email,
                    token = token
                )

                val response = RetrofitInstance.userManagementService.verifyOTP(request)

                if (response.isSuccessful) {
                    if (type == "recovery") {
                        // После успешного ввода кода для восстановления → экран нового пароля
                        navController.navigate("new_password/$email")
                    } else {
                        // После подтверждения регистрации → на логин
                        navController.navigate("login") {
                            popUpTo("register") { inclusive = true }
                        }
                    }
                } else {
                    when (response.code()) {
                        409 -> errorMessage.value = "Пользователь уже существует"
                        403 -> errorMessage.value = "Неверный код подтверждения"
                        400 -> errorMessage.value = "Неверный формат email или пароля"
                        429 -> errorMessage.value = "Слишком много запросов, повторите позднее"
                        else -> errorMessage.value = "Ошибка сервера: ${response.code()}"
                    }
                }
            } catch (e: Exception) {
                errorMessage.value = "Ошибка: ${e.message}"
            }
        }
    }
}
