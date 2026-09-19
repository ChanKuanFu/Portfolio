package com.example.EcoWise.UserModule

import java.util.UUID

data class UserAccount(
    // unique ID, default random generate UUID
    // easier to store in supabase
    val id: String = UUID.randomUUID().toString(),
    val fullName: String,
    val email: String,
    val profilePictureUrl: String? = null,
    val isGoogleAccount: Boolean = false,
    val createdAt: Long = System.currentTimeMillis() // Account create time
)