package com.example.botinki

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.botinki.data.UserSession
import com.example.botinki.ui.theme.LaptevTheme
import com.example.botinki.ui.view.ForgotPasswordScreen
import com.example.botinki.ui.view.HomeScreen
import com.example.botinki.ui.view.NewPasswordScreen
import com.example.botinki.ui.view.Onboard1Screen
import com.example.botinki.ui.view.Onboard2Screen
import com.example.botinki.ui.view.Onboard3Screen
import com.example.botinki.ui.view.ProfileScreen
import com.example.botinki.ui.view.VerifyOTPScreen
import com.example.botinki.ui.view.LoginScreen
import com.example.botinki.ui.view.RegisterScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            LaptevTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "onboard1",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("onboard1") { Onboard1Screen(navController) }
                        composable("onboard2") { Onboard2Screen(navController) }
                        composable("onboard3") { Onboard3Screen(navController) }
                        composable("login") { LoginScreen(navController = navController) }
                        composable("home") { HomeScreen(navController = navController) }
                        composable("register") { RegisterScreen(navController = navController) }
                        composable(
                            route = "verifyOTP/{email}/{type}",
                            arguments = listOf(
                                navArgument("email") { type = NavType.StringType },
                                navArgument("type") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val email = backStackEntry.arguments?.getString("email") ?: ""
                            val type = backStackEntry.arguments?.getString("type") ?: "signup"
                            VerifyOTPScreen(
                                navController = navController,
                                email = email,
                                otpType = type
                            )}
                        composable("forgot_password") {
                            ForgotPasswordScreen(navController)
                        }
                        composable(
                            route = "new_password/{email}",
                            arguments = listOf(
                                navArgument("email") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val email = backStackEntry.arguments?.getString("email") ?: ""
                            NewPasswordScreen(navController = navController, email = email)
                        }
                        composable("profile") {
                            val userId = UserSession.userId
                            val accessToken = UserSession.accessToken
                            Log.d("Nav", "UId: $userId")
                            Log.d("Nav", "AccessToken: $accessToken")
                            if (userId != null && accessToken != null) {
                                ProfileScreen(
                                    navController = navController,
                                    userId = userId,
                                    accessToken = accessToken
                                )
                            } else {
                                LoginScreen(navController = navController)
                            }
                        }
                    }
                }
            }
        }
    }
}

