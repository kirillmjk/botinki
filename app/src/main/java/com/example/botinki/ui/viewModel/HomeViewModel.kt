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
import com.example.botinki.data.model.FavouriteRequest
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    // список id избранных товаров
    val favouriteProducts = mutableStateListOf<String>()

    fun addToFavourite(productId: String, userId: String) {

        viewModelScope.launch {
            try {

                val request = FavouriteRequest(
                    product_id = productId,
                    user_id = userId
                )

                val response =
                    RetrofitInstance.userManagementService
                        .addToFavourite(request)

                if (response.isSuccessful) {
                    if (!favouriteProducts.contains(productId)) {
                        favouriteProducts.add(productId)
                    }
                }

            } catch (e: Exception) {
                Log.e("Favourite", e.message.toString())
            }
        }
    }
}