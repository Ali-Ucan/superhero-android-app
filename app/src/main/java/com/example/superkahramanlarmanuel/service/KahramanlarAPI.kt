package com.example.superkahramanlarmanuel.service

import com.example.superkahramanlarmanuel.model.CharacterResponse
import com.example.superkahramanlarmanuel.model.Kahramanlar
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface KahramanlarAPI {

    @GET("characters")
    suspend fun getData(
        @Query("ts") timestamp: String,
        @Query("apikey") apikey: String,
        @Query("hash") hash: String,
        @Query("limit") limit: Int
    ): CharacterResponse // Kahramanlar değil, tüm API yanıtını temsil eden sınıf

    @GET("characters/{characterId}")
    suspend fun getCharacterById(
        @Path("characterId")characterId: Int,
        @Query("ts") timestamp: String,
        @Query("apikey") apikey: String,
        @Query("hash") hash: String
    ): CharacterResponse

    @GET("characters")
    suspend fun searchCharacters(
        @Query("ts") timestamp: String,
        @Query("apikey") apikey: String,
        @Query("hash") hash: String,
        @Query("nameStartsWith") name: String // Bu parametre ile arama yapacağız
    ): CharacterResponse



}