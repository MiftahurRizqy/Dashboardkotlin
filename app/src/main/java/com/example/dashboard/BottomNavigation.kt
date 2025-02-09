package com.example.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController

@Composable
fun AppScaffold(
    navController: NavHostController,
    content: @Composable () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Content area
            Box(modifier = Modifier.weight(1f)) {
                content()
            }
            // Bottom navigation
            AppBottomNavigation(navController)
        }
    }
}

@Composable
fun AppBottomNavigation(navController: NavHostController) {
    NavigationBar(
        modifier = Modifier.background(Color(0xFFF96E2A)),
        containerColor = Color(0xFFF96E2A),
    ) {
        NavigationBarItem(
            icon = { Icon(imageVector = Icons.Filled.Home, contentDescription = "Menu") },
            label = { Text("Dashboard") },
            selected = false,
            onClick = { navController.navigate("dashboard") }
        )
        NavigationBarItem(
            icon = { Icon(imageVector = Icons.Filled.Build, contentDescription = "Devices") },
            label = { Text("Devices") },
            selected = false,
            onClick = { navController.navigate("devices") }
        )
        NavigationBarItem(
            icon = { Icon(imageVector = Icons.Filled.AccountCircle, contentDescription = "Profile") },
            label = { Text("Profile") },
            selected = false,
            onClick = { navController.navigate("profile") }
        )
    }
}