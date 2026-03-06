package com.example.botinki.ui.viewModel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.botinki.data.RetrofitInstance
import com.example.botinki.data.UserSession
import com.example.botinki.data.model.ProfileRequest
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    var showDialog = mutableStateOf(false)
    var dialogText = mutableStateOf("")
    val errorMessage = mutableStateOf<String?>(null)

    fun editProfile(
        userId: String,
        photo: String,
        firstname: String,
        lastname: String,
        address: String,
        phone: String
    ) {
        viewModelScope.launch {
            try {
                val accessToken = UserSession.accessToken
                if (accessToken == null) {
                    // обработка ошибки
                    return@launch
                }

                val request = ProfileRequest(
                    user_id = UserSession.userId.toString(),
                    photo = photo,
                    firstname = firstname,
                    lastname = lastname,
                    address = address,
                    phone = phone
                )

                // ИСПРАВЛЕНО: добавили userIdFilter
                val response = RetrofitInstance.userManagementService
                    .editProfile(
                        authHeader = "Bearer $accessToken",
                        userIdFilter = "eq.${UserSession.userId}",  // ВАЖНО!
                        body = request
                    )

                if (response.isSuccessful) {
                    // успех
                } else {
                    // обработка ошибки
                }
            } catch (e: Exception) {
                // обработка исключения
            }
        }
    }
}