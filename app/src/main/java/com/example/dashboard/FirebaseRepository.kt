package com.example.dashboard

import com.google.firebase.firestore.FirebaseFirestore

class FirebaseRepository {
    private val firestore = FirebaseFirestore.getInstance()

    // Fungsi lainnya seperti addOrUpdateDevice, getDevices, dll.
    fun addOrUpdateDevice(device: Device) {
        val collection = firestore.collection("devices")
        val document = if (device.id.isNotEmpty()) collection.document(device.id) else collection.document()

        document.set(device)
            .addOnSuccessListener {
                println("Device added/updated successfully!")
            }
            .addOnFailureListener { error ->
                println("Error adding/updating device: ${error.message}")
            }
    }

    fun getDevices(
        onSuccess: (List<Device>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firestore.collection("devices")
            .get()
            .addOnSuccessListener { result ->
                val devices = result.map { document ->
                    document.toObject(Device::class.java).copy(id = document.id)
                }
                onSuccess(devices)
            }
            .addOnFailureListener { error ->
                onFailure(error)
            }
    }
    // Fungsi untuk menghapus device berdasarkan ID
    fun deleteDevice(
        deviceId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firestore.collection("devices")
            .document(deviceId)
            .delete()
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }
}
