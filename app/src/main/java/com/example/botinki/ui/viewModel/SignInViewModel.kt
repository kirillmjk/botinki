package com.example.botinki.ui.viewModel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.botinki.data.RetrofitInstance
import com.example.botinki.data.UserSession
import com.example.botinki.data.model.SignInRequest
import kotlinx.coroutines.launch
import kotlin.math.log

class SignInViewModel : ViewModel() {


    var showDialog = mutableStateOf(false)
    var dialogText = mutableStateOf("")
    val errorMessage = mutableStateOf<String?>(null)

    fun signIn(signInRequest: SignInRequest, navController: NavController) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.userManagementService.signIn(signInRequest)
                Log.d("SignIn", "SignIn ${response.isSuccessful.toString()}")
                if (response.isSuccessful) {
                    val body = response.body()
                    Log.d("SignIn", "SignIn BODY $body")
                    if (body != null) {
                        val accessToken = body.access_token
                        val userId = body.user.id

                        UserSession.accessToken = accessToken
                        UserSession.userId = userId
                    }

                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                } else {
                    when (response.code()) {
                        409 -> errorMessage.value = "Пользователь уже существует"
                        400 -> errorMessage.value = "Неверный формат почты или пароля"
                        429 -> errorMessage.value = "Слишком много запросов, повторите позднее"
                        else -> errorMessage.value = "Ошибка сервера: ${response.code()}"
                    }
                    showDialog.value = true
                }
            } catch (e: Exception) {
                dialogText.value = "Ошибка: ${e.message}"
                showDialog.value = true
            }
        }
    }
}
