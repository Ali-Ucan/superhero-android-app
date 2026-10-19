package com.example.superkahramanlarmanuel.repository

import android.util.Log
import com.example.superkahramanlarmanuel.model.Kahramanlar
import com.example.superkahramanlarmanuel.model.toKahramanlar
import com.example.superkahramanlarmanuel.service.KahramanlarAPI
import java.security.MessageDigest
import com.example.superkahramanlarmanuel.model.FavoritesManager

class MarvelRepository(
    private val apiService : KahramanlarAPI,
    private val favoritesManager: FavoritesManager
) {
    private val publicKey = "Your_Key"
    private val privateKey = "Your_Key"

    private fun getMarvelHash(ts: String, privateKey: String, publicKey: String): String {
        val input = ts + privateKey + publicKey
        val md = MessageDigest.getInstance("MD5")
        val digest = md.digest(input.toByteArray())
        return digest.joinToString("") { "%02x".format(it) }
    }

    suspend fun getCharacters(): List<Kahramanlar> {
        val ts = System.currentTimeMillis().toString()
        val hash = getMarvelHash(ts, privateKey, publicKey)

        return try {
            val response = apiService.getData(ts, publicKey, hash, 100)
            val favoriteIds = favoritesManager.getFavoriteIds()
            response.data.results.map { character ->
                character.toKahramanlar(favoriteIds.contains(character.id.toString()))
            }
        } catch (e: Exception) {
            Log.e("MarvelRepository", "API isteği başarısız: ${e.message}")
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getCharacterById(characterId: Int): Kahramanlar? {
        val ts = System.currentTimeMillis().toString()
        val hash = getMarvelHash(ts, privateKey, publicKey)

        return try {
            val response = apiService.getCharacterById(characterId, ts, publicKey, hash)
            response.data.results.firstOrNull()?.toKahramanlar(
                favoritesManager.isFavorite(characterId)
            )
        } catch (e: Exception) {
            Log.e("MarvelRepository", "ID ile karakter yüklenemedi: ${e.message}")
            e.printStackTrace()
            null
        }
    }

    suspend fun searchCharacters(query: String): List<Kahramanlar> {
        val ts = System.currentTimeMillis().toString()
        val hash = getMarvelHash(ts, privateKey, publicKey)

        return try {
            val response = apiService.searchCharacters(ts, publicKey, hash, query)
            val favoriteIds = favoritesManager.getFavoriteIds()
            response.data.results.map { character ->
                character.toKahramanlar(favoriteIds.contains(character.id.toString()))
            }
        } catch (e: Exception) {
            Log.e("MarvelRepository", "Arama sonuçları yüklenemedi: ${e.message}")
            e.printStackTrace()
            emptyList()
        }
    }

    fun toggleFavorite(kahraman: Kahramanlar): Kahramanlar {
        val newIsFavorite = !kahraman.isFavorite
        if (newIsFavorite) {
            favoritesManager.addFavorite(kahraman.id.toString())
        } else {
            favoritesManager.removeFavorite(kahraman.id.toString())
        }
        return kahraman.copy(isFavorite = newIsFavorite)
    }
}