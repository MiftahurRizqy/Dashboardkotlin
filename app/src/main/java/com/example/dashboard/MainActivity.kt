package com.example.dashboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dashboard.ui.theme.DashboardTheme
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inisialisasi Firebase
        FirebaseApp.initializeApp(this)
        setContent {
            DashboardTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "dashboard") {
                    composable("dashboard") {
                        AppScaffold(navController) {
                            DashboardScreen() // Memanggil DashboardScreen dari file baru
                        }
                    }
                    composable("devices") {
                        AppScaffold(navController) {
                            DevicesScreen()
                        }
                    }
                    composable("profile") {
                        AppScaffold(navController) {
                            ProfileScreen()
                        }
                    }
                }
            }
        }
    }
}


