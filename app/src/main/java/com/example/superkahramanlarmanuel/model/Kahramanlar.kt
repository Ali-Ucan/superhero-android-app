package com.example.superkahramanlarmanuel.model

data class Kahramanlar(
    val id: Int,
    val name: String,
    val description: String,
    val thumbnailUrl: String,
    val isFavorite: Boolean = false
) {
    fun getImageUrl(): String {
        return thumbnailUrl.replace("http://", "https://")
    }
}

data class Thumbnail(
    val path: String,
    val extension: String
)