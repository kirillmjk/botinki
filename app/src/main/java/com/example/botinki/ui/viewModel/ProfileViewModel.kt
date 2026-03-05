package com.example.botinki.ui.viewModel

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.botinki.data.RetrofitInstance
import com.example.botinki.data.UserSession
import com.example.botinki.data.model.FavouriteRequest
import com.example.botinki.data.model.ProfileRequest
import com.example.botinki.ui.view.ProfileScreen
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {


    fun editProfile(userId: String, photo: String, firstname: String, lastname: String, address: String, phone: String) {var showDialog = mutableStateOf(false)
        var dialogText = mutableStateOf("")
        val errorMessage = mutableStateOf<String?>(null)
        viewModelScope.launch {
            try {

                val request = ProfileRequest(
                    user_id = UserSession.userId.toString(),
                    photo = photo,
                    firstname = firstname,
                    lastname = lastname,
                    address = address,
                    phone = phone
                )

                val response =
                    RetrofitInstance.userManagementService
                        .editProfile(authHeader = "Bearer $UserSession.accessToken",body = request)

                if (response.isSuccessful) {

                }else {
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
                Log.e("Profile", e.message.toString())
            }
        }
    }
}