package com.example.shift_e.data.repository

import com.example.shift_e.model.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object UserRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    suspend fun fetchUserData(): UserData? {
        val uid = auth.currentUser?.uid ?: return null
        val doc = db.collection("users").document(uid).get().await()
        return UserData(
            firstName = doc.getString("firstName") ?: "",
            lastName = doc.getString("lastName") ?: "",
            birthday = doc.getString("birthday") ?: "",
            email = doc.getString("email") ?: "N/A",
            mobile = doc.getString("mobile") ?: "N/A",
            totalRides = doc.getLong("totalRides")?.toInt() ?: 0,
            moneySaved = doc.getLong("moneySaved")?.toInt() ?: 0,
            rating = doc.getDouble("rating") ?: 0.0
        )
    }
}
