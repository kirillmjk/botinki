package com.example.botinki.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.botinki.R
import com.example.botinki.data.RetrofitInstance
import com.example.botinki.data.UserSession
import com.example.botinki.data.model.FavouriteRequest
import com.example.botinki.data.service.ProductDto
import kotlinx.coroutines.launch
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    navController: NavHostController,
    productId: String
) {
    val token = UserSession.accessToken
    val userId = UserSession.userId
    val scope = rememberCoroutineScope()

    var allProducts by remember { mutableStateOf<List<CatalogProduct>>(emptyList()) }
    var current by remember { mutableStateOf<CatalogProduct?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(productId, token, userId) {
        if (token == null || userId == null) return@LaunchedEffect
        isLoading = true
        try {
            val service = RetrofitInstance.userManagementService
            val products: List<ProductDto> = service.getProducts(authHeader = "Bearer $token")
            val favs = service.getFavourites(authHeader = "Bearer $token", userIdFilter = "eq.$userId")
            val favSet = favs.mapNotNull { it.product_id }.toSet()

            val mapped = products.map { p ->
                CatalogProduct(
                    id = p.id,
                    title = p.title,
                    price = p.cost,
                    categoryId = p.category_id,
                    isBestSeller = p.is_best_seller == true,
                    imageRes = R.drawable.img_shoe_blue,
                    isFavorite = favSet.contains(p.id),
                    description = p.description
                )
            }
            allProducts = mapped
            current = mapped.firstOrNull { it.id == productId } ?: mapped.firstOrNull()
        } finally {
            isLoading = false
        }
    }

    fun toggleFavourite(product: CatalogProduct, isFav: Boolean) {
        if (token == null || userId == null) return
        scope.launch {
            try {
                val service = RetrofitInstance.userManagementService
                if (isFav) {
                    service.addFavourite(
                        authHeader = "Bearer $token",
                        body = FavouriteRequest(user_id = userId, product_id = product.id)
                    )
                } else {
                    service.deleteFavourite(
                        authHeader = "Bearer $token",
                        userIdFilter = "eq.$userId",
                        productIdFilter = "eq.${product.id}"
                    )
                }
                allProducts = allProducts.map { if (it.id == product.id) it.copy(isFavorite = isFav) else it }
                current = current?.let { if (it.id == product.id) it.copy(isFavorite = isFav) else it }
            } catch (_: Exception) { }
        }
    }

    val product = current

    Scaffold(containerColor = Color(0xFFF5F7FB)) { innerPadding ->
        if (isLoading || product == null) {
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF48B2E7))
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(allProducts, product) {
                        detectHorizontalDragGestures { _, dragAmount ->
                            if (abs(dragAmount) > 50f) {
                                val currentIndex = allProducts.indexOfFirst { it.id == product.id }
                                if (dragAmount < 0) {
                                    // Свайп влево → следующий
                                    val nextIndex = (currentIndex + 1) % allProducts.size
                                    val nextProduct = allProducts[nextIndex]
                                    navController.navigate("details/${nextProduct.id}")
                                    {
                                        popUpTo("details/$productId") { inclusive = true }
                                        launchSingleTop = true
                                    }
                                } else {
                                    // Свайп вправо → предыдущий
                                    val prevIndex = (currentIndex - 1 + allProducts.size) % allProducts.size
                                    val prevProduct = allProducts[prevIndex]
                                    navController.navigate("details/${prevProduct.id}"){
                                        popUpTo("details/$productId") { inclusive = true }
                                        launchSingleTop = true
                                    }
                                }
                            }
                        }
                    }
            ) {
                Column(
                    modifier = Modifier
                        .padding(
                            start = innerPadding.calculateLeftPadding(LocalLayoutDirection.current),
                            end = innerPadding.calculateRightPadding(LocalLayoutDirection.current),
                            bottom = innerPadding.calculateBottomPadding()
                        )
                        .fillMaxSize()
                        .background(Color(0xFFF5F7FB))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                painter = painterResource(id = R.drawable.back_button),
                                contentDescription = "Назад"
                            )
                        }
                        Text(
                            text = "ShoeStore",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        IconButton(onClick = { toggleFavourite(product, !product.isFavorite) }) {
                            Icon(
                                painter = painterResource(
                                    id = if (product.isFavorite) R.drawable.ic_heart_fill else R.drawable.ic_favorite_border
                                ),
                                contentDescription = "Favorite",
                                tint = if (product.isFavorite) Color(0xFFDD4B4B) else Color(0xFFB0B0B0)
                            )
                        }
                    }

                    // --- Content ---
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp)
                    ) {
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = product.title,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF222222)
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Men's Shoes",
                            fontSize = 13.sp,
                            color = Color(0xFF9E9E9E)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "₽${product.price}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF222222)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                                .clip(RoundedCornerShape(24.dp))
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = product.imageRes),
                                contentDescription = product.title,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Fit
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Описание",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF222222)
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = product.description,
                            fontSize = 13.sp,
                            color = Color(0xFF555555)
                        )
                    }

                    // --- Footer ---
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                                .clickable { toggleFavourite(product, !product.isFavorite) },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(
                                    id = if (product.isFavorite) R.drawable.ic_heart_fill else R.drawable.ic_favorite_border
                                ),
                                contentDescription = "Favorite",
                                tint = if (product.isFavorite) Color(0xFFDD4B4B) else Color(0xFFB0B0B0)
                            )
                        }

                        Button(
                            onClick = { /* TODO: добавить в корзину */ },
                            modifier = Modifier
                                .weight(1f)
                                .height(52.dp),
                            shape = RoundedCornerShape(18.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF48B2E7))
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_cart),
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "В корзину",
                                fontSize = 15.sp,
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}