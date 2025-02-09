package com.example.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.dashboard.ui.theme.DashboardTheme


// Data class for Device
data class Device(
    val id: String = "",
    val bodyCode: String = "",
    val deviceCode: String = "",
    val deviceName: String = "",
    val serialNumber: String = "",
    val deviceModel: String = "",
    val city: String = ""
)

@Composable
fun DevicesScreen() {
    var devices by remember { mutableStateOf(listOf<Device>()) }
    val firebaseRepo = FirebaseRepository()  // Initialize Firebase repository
    var showDialog by remember { mutableStateOf(false) }
    var selectedDevice by remember { mutableStateOf<Device?>(null) }

    // Form states
    var bodyCode by remember { mutableStateOf("") }
    var deviceCode by remember { mutableStateOf("") }
    var deviceName by remember { mutableStateOf("") }
    var serialNumber by remember { mutableStateOf("") }
    var deviceModel by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }

    // Load devices when composable is first loaded
    LaunchedEffect(true) {
        firebaseRepo.getDevices(
            onSuccess = { fetchedDevices ->
                devices = fetchedDevices
            },
            onFailure = { error ->
                println("Error getting devices: ${error.message}")
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xFFFBF8EF))
    ) {
        // Top Bar
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Devices Management",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    selectedDevice = null
                    showDialog = true
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF088395)), // Warna tombol
                modifier = Modifier
                    .align(Alignment.Start)
                    .height(36.dp)
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add Device",
                    modifier = Modifier.size(16.dp),
                    tint = Color.White // Warna ikon putih
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    "Add",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.White) // Teks putih
                )
            }
        }


        Spacer(modifier = Modifier.height(16.dp))

        // Devices List
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(devices) { device ->
                DeviceCard(
                    device = device,
                    onEdit = {
                        selectedDevice = device
                        bodyCode = device.bodyCode
                        deviceCode = device.deviceCode
                        deviceName = device.deviceName
                        serialNumber = device.serialNumber
                        deviceModel = device.deviceModel
                        city = device.city
                        showDialog = true
                    },
                    onDelete = { deviceToDelete ->
                        // Hapus perangkat dari Firestore
                        firebaseRepo.deleteDevice(deviceToDelete.id, onSuccess = {
                            // Perbarui daftar perangkat di UI setelah perangkat dihapus
                            devices = devices.filter { it.id != deviceToDelete.id }
                        }, onFailure = { error ->
                            println("Error deleting device: ${error.message}")
                        })
                    }
                )
            }
        }
    }

    // Add/Edit Dialog
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(if (selectedDevice == null) "Add Device" else "Edit Device") },
            text = {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = bodyCode,
                        onValueChange = { bodyCode = it },
                        label = { Text("Body Code") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = deviceCode,
                        onValueChange = { deviceCode = it },
                        label = { Text("Device Code") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = deviceName,
                        onValueChange = { deviceName = it },
                        label = { Text("Device Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = serialNumber,
                        onValueChange = { serialNumber = it },
                        label = { Text("Serial Number") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = deviceModel,
                        onValueChange = { deviceModel = it },
                        label = { Text("Device Model") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = city,
                        onValueChange = { city = it },
                        label = { Text("City") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val newDevice = Device(
                            id = selectedDevice?.id ?: devices.size.toString(),
                            bodyCode = bodyCode,
                            deviceCode = deviceCode,
                            deviceName = deviceName,
                            serialNumber = serialNumber,
                            deviceModel = deviceModel,
                            city = city
                        )

                        // Save or update device in Firestore
                        firebaseRepo.addOrUpdateDevice(newDevice)

                        devices = if (selectedDevice == null) {
                            devices + newDevice
                        } else {
                            devices.map { if (it.id == selectedDevice?.id) newDevice else it }
                        }

                        showDialog = false
                        bodyCode = ""
                        deviceCode = ""
                        deviceName = ""
                        serialNumber = ""
                        deviceModel = ""
                        city = ""
                        selectedDevice = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF088395))
                ) {
                    Text(if (selectedDevice == null) "Add" else "Update", color = Color.White)
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF088395))
                ) {
                    Text("Cancel", color = Color.White)
                }
            }
        )
    }
}

@Composable
private fun DeviceCard(
    device: Device,
    onEdit: () -> Unit,
    onDelete: (Device) -> Unit // Mengubah onDelete untuk menerima Device sebagai parameter
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFC9E6F0)), // Warna kartu
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = device.deviceName,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Body Code: ${device.bodyCode}")
                    Text("Device Code: ${device.deviceCode}")
                    Text("Serial Number: ${device.serialNumber}")
                    Text("Device Model: ${device.deviceModel}")
                    Text("City: ${device.city}")
                }
                Row {
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit", tint = MaterialTheme.colorScheme.primary)
                    }
                    IconButton(onClick = { onDelete(device) }) { // Memanggil onDelete dengan perangkat yang sesuai
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DevicesScreenPreview() {
    DashboardTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFFFBF8EF) // Background warna halaman untuk preview
        ) {
            // Menggunakan AppScaffold untuk preview agar terlihat bottom navigation
            AppScaffold(rememberNavController()) {
                DevicesScreen()
            }
        }
    }
}