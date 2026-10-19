package com.example.superkahramanlarmanuel.model

import android.content.Context
import android.content.SharedPreferences

class FavoritesManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("favorites", Context.MODE_PRIVATE)

    fun getFavoriteIds(): Set<String> {
        return sharedPreferences.getStringSet("favorite_ids", emptySet()) ?: emptySet()
    }

    fun isFavorite(characterId: Int): Boolean {
        return getFavoriteIds().contains(characterId.toString())
    }

    fun addFavorite(characterId: String) {
        val editor = sharedPreferences.edit()
        val favorites = getFavoriteIds().toMutableSet()
        favorites.add(characterId)
        editor.putStringSet("favorite_ids", favorites)
        editor.apply()
    }

    fun removeFavorite(characterId: String) {
        val editor = sharedPreferences.edit()
        val favorites = getFavoriteIds().toMutableSet()
        favorites.remove(characterId)
        editor.putStringSet("favorite_ids", favorites)
        editor.apply()
    }
}