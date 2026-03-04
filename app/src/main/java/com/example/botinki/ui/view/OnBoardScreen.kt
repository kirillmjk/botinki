package com.example.botinki.ui.view

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.botinki.R

@SuppressLint("Range")
@Composable
fun Onboard1Screen(navController: NavHostController) {
    var totalDrag by remember { mutableStateOf(0f) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF48B2E7),
                        Color(0xFF44A9DC),
                        Color(0xFF2B6B8B)
                    )
                )
            )
    ) {
        // Отдельный слой для обработки свайпов (поверх всего, кроме кнопки)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onHorizontalDrag = { _, dragAmount ->
                            totalDrag += dragAmount
                        },
                        onDragEnd = {
                            when {
                                totalDrag > 200 -> navController.navigate("onboard3")
                                totalDrag < -200 -> navController.navigate("onboard2")
                            }
                            totalDrag = 0f // Сбрасываем после навигации
                        },
                        onDragStart = { totalDrag = 0f } // Сбрасываем при старте
                    )
                }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "ДОБРО\nПОЖАЛОВАТЬ",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(180.dp))
                Image(
                    painter = painterResource(id = R.drawable.onboard1),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth(1.15f)
                        .height(320.dp)
                        .offset(x = 30.dp, y = (-20).dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    repeat(3) { index ->
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .width(if (index == 0) 24.dp else 8.dp)
                                .height(6.dp)
                                .background(
                                    color = if (index == 0) Color.White else Color(0x55FFFFFF),
                                    shape = RoundedCornerShape(3.dp)
                                )
                        )
                    }
                }
            }

            Button(
                onClick = { navController.navigate("onboard2") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF48B2E7)
                )
            ) {
                Text(text = "Начать", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Composable
fun Onboard2Screen(navController: NavHostController) {
    OnboardBase(
        imageRes = R.drawable.onboard2,
        title = "Начнем\nпутешествие",
        subtitle = "Умная, великолепная и модная\nколлекция. Изучите сейчас",
        buttonText = "Далее",
        onButtonClick = { navController.navigate("onboard3") },
        indicatorIndex = 1,
        navController = navController,
        nextRoute = "onboard1",
        prevRoute = "onboard3"
    )
}

@Composable
fun Onboard3Screen(navController: NavHostController) {
    OnboardBase(
        imageRes = R.drawable.onboard3,
        title = "У Вас Есть Сила,\nЧтобы",
        subtitle = "В Вашей Комнате Много Красивых\nИ Привлекательных Растений",
        buttonText = "Далее",
        onButtonClick = { navController.navigate("register") },
        indicatorIndex = 2,
        navController = navController,
        nextRoute = "onboard2",
        prevRoute = "register"
    )
}

@SuppressLint("Range")
@Composable
private fun OnboardBase(
    imageRes: Int,
    title: String,
    subtitle: String,
    buttonText: String,
    onButtonClick: () -> Unit,
    indicatorIndex: Int,
    navController: NavHostController,
    nextRoute: String,
    prevRoute: String
) {
    var totalDrag by remember { mutableStateOf(0f) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF48B2E7),
                        Color(0xFF44A9DC),
                        Color(0xFF2B6B8B)
                    )
                )
            )
    ) {
        // Слой для обработки свайпов
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onHorizontalDrag = { _, dragAmount ->
                            totalDrag += dragAmount
                        },
                        onDragEnd = {
                            when {
                                totalDrag > 200 -> navController.navigate(nextRoute)
                                totalDrag < -200 -> navController.navigate(prevRoute)
                            }
                            totalDrag = 0f
                        },
                        onDragStart = { totalDrag = 0f }
                    )
                }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(130.dp))

                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(340.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = title,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                if (subtitle.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = subtitle,
                        fontSize = 14.sp,
                        color = Color(0xFFE0E0E0),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    repeat(3) { index ->
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .width(if (index == indicatorIndex) 24.dp else 8.dp)
                                .height(6.dp)
                                .background(
                                    color = if (index == indicatorIndex) Color.White else Color(0x55FFFFFF),
                                    shape = RoundedCornerShape(3.dp)
                                )
                        )
                    }
                }
            }

            Button(
                onClick = onButtonClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF48B2E7)
                )
            ) {
                Text(text = buttonText, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}