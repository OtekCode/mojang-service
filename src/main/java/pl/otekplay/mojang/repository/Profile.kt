package pl.otekplay.mojang.repository

import java.util.*

data class Profile(
    val id: UUID,
    val name: String,
    val premium: Boolean,
    val lastCheck: Long
)