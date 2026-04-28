package com.example.doublescreen.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Route {
    @Serializable
    data object List1 : Route
    
    @Serializable
    data object List2 : Route
    
    @Serializable
    data object Profile : Route
}

@Serializable
data class Detail(val id: Int)

@Serializable
data class UserDetail(val userId: String)
