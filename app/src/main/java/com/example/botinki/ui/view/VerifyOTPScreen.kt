package com.example.tyagi_shop.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.tyagi_shop.R
import com.example.tyagi_shop.ui.theme.MosyaginTheme
import com.example.tyagi_shop.ui.viewModel.VerifyOTPViewModel

@Composable
fun VerifyOTPScreen(
    navController: NavHostController,
    email: String,
    otpType: String = "signup", // "signup" или "recovery"
    viewModel: VerifyOTPViewModel = viewModel()
) {
    val otpStandartColor = colorResource(id = R.color.otpStandart)
    val otpRedColor = colorResource(id = R.color.otpRed)
    var otpBackgroundColor by remember { mutableStateOf(otpStandartColor) }

    var otpValue by remember { mutableStateOf(TextFieldValue("")) }
    val context = LocalContext.current
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(otpValue.text) {
        if (otpValue.text.length == 8) {
            viewModel.verifyOTP(email, otpValue.text, otpType, context, navController)
            otpBackgroundColor = otpStandartColor // Зеленый при отправке
        }
    }

    LaunchedEffect(viewModel.errorMessage.value) {
        viewModel.errorMessage.value?.let { msg ->
            errorMessage = msg
            showErrorDialog = true
            otpBackgroundColor = otpRedColor // КРАСНЫЙ при ошибке!
            viewModel.errorMessage.value = null
        }
    }
    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = {
                showErrorDialog = false
                errorMessage = ""
                otpBackgroundColor = otpRedColor
            },
            title = { Text("Ошибка") },
            text = { Text(errorMessage) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showErrorDialog = false
                        errorMessage = ""
                        otpBackgroundColor = otpRedColor
                    }
                ) {
                    Text("OK")
                }
            }
        )

    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(50.dp))

            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
                    .background(Color(0xFFF7F7F7))
                    .clickable { navController.popBackStack() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.back_button),
                    contentDescription = "Назад",
                    tint = Color(0xFF555555)
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "OTP Проверка",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Пожалуйста, проверьте свою\nэлектронную почту, чтобы увидеть код\nподтверждения",
                fontSize = 14.sp,
                color = Color(0xFF7D7D7D),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "OTP Код",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF333333),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OtpInputField(
                modifier = Modifier
                    .height(20.dp)
                    .background(otpBackgroundColor, RoundedCornerShape(12.dp))
                    .width(16.dp),
                otpValue = otpValue,
                onValueChange = {
                    if (it.text.length <= 8) {
                        otpValue = it
                    }
                },
                length = 8,
                backgroundColor = otpBackgroundColor, // Передаем текущий цвет фона
                errorColor = otpRedColor
            )

            Spacer(modifier = Modifier.height(16.dp))


        }
    }
    }


@Composable
fun OtpInputField(
    modifier: Modifier,
    otpValue: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    length: Int,
    backgroundColor: Color, // Добавьте этот параметр
    errorColor: Color
) {
    Box(
        contentAlignment = Alignment.CenterStart
    ) {
        BasicTextField(
            value = otpValue,
            onValueChange = onValueChange,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            decorationBox = {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    repeat(length) { index ->
                        val char = if (index < otpValue.text.length) otpValue.text[index] else null
                        val isFocused = index == otpValue.text.length

                        OtpCell(char = char, isFocused = isFocused, backgroundColor = backgroundColor,
                            errorColor = errorColor )
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(color = Color.Transparent)
        )
    }
}

@Composable
fun OtpCell(
    char: Char?,
    isFocused: Boolean,
    backgroundColor: Color, // Добавьте этот параметр
    errorColor: Color
) {
    val borderColor = if (isFocused) Color(0xFFFF5252) else Color(0xFFF7F7F7)
    // Используем backgroundColor для фона ячейки
    val cellBackgroundColor = if (backgroundColor == errorColor) {
        errorColor.copy(alpha = 0.3f) // Делаем цвет полупрозрачным для ошибки
    } else {
        Color(0xFFF7F7F7) // Стандартный цвет для ячеек
    }

    Box(
        modifier = Modifier
            .width(48.dp)
            .height(60.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(cellBackgroundColor) // Используем измененный цвет
            .border(
                width = if (isFocused) 1.dp else 0.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = char?.toString() ?: "",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = if (backgroundColor == errorColor) Color.Red else Color.Black, // Текст красный при ошибке
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun VerifyOTPScreenPreview() {
    MosyaginTheme {
        val navController = rememberNavController()
        VerifyOTPScreen(
            navController = navController,
            email = "test@example.com",
            otpType = "recovery"
        )
    }
}
