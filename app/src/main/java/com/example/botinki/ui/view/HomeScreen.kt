package com.example.botinki.ui.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.botinki.R
import com.example.botinki.data.UserSession
import com.example.botinki.ui.viewModel.HomeViewModel
import com.example.botinki.ui.viewModel.SignUpViewModel

data class Product(
    val id: String,
    val name: String,
    val price: String,
    val imageRes: Int
)

@Composable
fun HomeScreen(navController: NavHostController, viewModel: SignUpViewModel = viewModel(), viewModel2: HomeViewModel = viewModel()) {
    val scrollState = rememberScrollState()
    val categories = listOf("Все", "Outdoor", "Tennis")
    var selectedCategory by remember { mutableStateOf("Все") }

    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    //val favouriteProducts = mutableStateListOf<String>()
    LaunchedEffect(viewModel.errorMessage.value) {
        viewModel.errorMessage.value?.let { msg ->
            errorMessage = msg
            showErrorDialog = true
            viewModel.errorMessage.value = null // Сбрасываем сообщение об ошибке
        }
    }
    // AlertDialog
    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = {
                showErrorDialog = false
                errorMessage = ""
            },
            title = { Text("Ошибка") },
            text = { Text(errorMessage) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showErrorDialog = false
                        errorMessage = ""
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }
    val products = listOf(
        Product("21c2d7c0-a2ea-49a2-9198-52bafafc6958", "Nike Air Max", "₽752.00", R.drawable.img_shoe_blue),
        Product("64229908-3713-4147-bedc-68ddaef9c67a", "Nike Air Max", "₽752.00", R.drawable.img_shoe_blue)
    )

    Scaffold(
        bottomBar = { BottomBar(navController = navController, currentRoute = "home") },
        containerColor = Color(0xFFF5F7FA)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color(0xFFF5F7FA))
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = stringResource(id = R.string.home_title),
                fontSize = 28.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF333333),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                SearchBox(
                    hint = stringResource(id = R.string.search_hint),
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF48B2E7))
                        .clickable { },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_filter),
                        contentDescription = "Filter",
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = stringResource(id = R.string.categories),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF333333)
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(categories) { category ->
                    val isSelected = category == selectedCategory
                    CategoryChip(
                        title = category,
                        selected = isSelected,
                        onClick = {
                            selectedCategory = category
                            navController.navigate("catalog/$category")
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.popular),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF333333)
                )
                Text(
                    text = stringResource(id = R.string.see_all),
                    fontSize = 14.sp,
                    color = Color(0xFF48B2E7)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(products) { product ->
                    ProductCard(
                        product = product,
                        viewModel = viewModel2,
                        navController = navController
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.promo),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF333333)
                )
                Text(
                    text = stringResource(id = R.string.see_all),
                    fontSize = 14.sp,
                    color = Color(0xFF48B2E7)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            PromoBanner()
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
private fun SearchBox(hint: String, modifier: Modifier = Modifier) {
    var value by remember { mutableStateOf(TextFieldValue("")) }

    OutlinedTextField(
        value = value,
        onValueChange = { value = it },
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = "Search",
                tint = Color(0xFFB0B0B0)
            )
        },
        placeholder = {
            Text(text = hint, color = Color(0xFFB0B0B0))
        },
        singleLine = true,
        modifier = modifier
            .height(48.dp)
            .clip(RoundedCornerShape(16.dp)),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        )
    )
}

@Composable
private fun CategoryChip(
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .height(34.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(if (selected) Color.White else Color(0xFFE8EDF3))
            .clickable { onClick() }
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            color = if (selected) Color(0xFF333333) else Color(0xFF828B99)
        )
    }
}

@Composable
private fun ProductCard(
    product: Product,
    viewModel: HomeViewModel,
    navController: NavHostController
) {
    var isFavourite by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .width(180.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(12.dp)
            .clickable{navController.navigate("details/${product.id}")}
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    modifier = Modifier.clickable {
                        isFavourite = !isFavourite
                    },
                    painter = if (isFavourite)
                        painterResource(id = R.drawable.ic_heart_fill4)
                    else
                        painterResource(id = R.drawable.ic_favorite_border),
                    contentDescription = "Favorite",
                    tint = if (isFavourite) Color.Red else Color(0xFFB0B0B0)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Image(
                painter = painterResource(id = product.imageRes),
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "BEST SELLER",
                fontSize = 10.sp,
                color = Color(0xFF48B2E7),
                fontWeight = FontWeight.Medium
            )

            Text(
                text = product.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF333333),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = product.price,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF333333)
                )
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF48B2E7))
                        .clickable { },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_cart),
                        contentDescription = "Add to cart",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun PromoBanner() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White)
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_addvertisment),
            contentDescription = "Promo",
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun BottomBar(navController: NavHostController, currentRoute: String) {
    val activeColor = Color(0xFF48B2E7)
    val inactiveColor = Color(0xFFB0B0B0)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Дом
            Icon(
                painter = painterResource(id = R.drawable.ic_home),
                contentDescription = "Home",
                tint = if (currentRoute == "home") activeColor else inactiveColor,
                modifier = Modifier.clickable {
                    if (currentRoute != "home") {
                        navController.navigate("home") {
                            popUpTo("home") { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                }
            )


            Icon(
                painter = painterResource(id = R.drawable.ic_heart),
                contentDescription = "Favorites",
                tint = if (currentRoute == "favorite") activeColor else inactiveColor,
                modifier = Modifier.clickable {
                    if (currentRoute != "favorite") {
                        navController.navigate("favorite") {
                            popUpTo("home") { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                }
            )


            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(activeColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.bag),
                    contentDescription = "Bag",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }


            Icon(
                painter = painterResource(id = R.drawable.ic_truck),
                contentDescription = "Orders",
                tint = inactiveColor
            )

            // Профиль
            Icon(
                painter = painterResource(id = R.drawable.ic_profile),
                contentDescription = "Profile",
                tint = if (currentRoute == "profile") activeColor else inactiveColor,
                modifier = Modifier.clickable {
                    if (currentRoute != "profile") {
                        navController.navigate("profile") {
                            popUpTo("home") { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                }
            )
        }
    }
}



