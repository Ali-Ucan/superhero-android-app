package com.example.superkahramanlarmanuel.model

import com.google.gson.annotations.SerializedName

data class CharacterResponse(
    @SerializedName("data")
    val data: Data
) {
    data class Data(
        @SerializedName("results")
        val results: List<Character>
    )

    data class Character(
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: String,
        @SerializedName("description")
        val description: String,
        @SerializedName("thumbnail")
        val thumbnail: Thumbnail,
        @SerializedName("comics")
        val comics: ComicList,
        @SerializedName("series")
        val series: SeriesList,
        @SerializedName("events")
        val events: EventList
    )

    data class Thumbnail(
        @SerializedName("path")
        val path: String,
        @SerializedName("extension")
        val extension: String
    )

    data class ComicList(
        @SerializedName("available")
        val available: Int,
        @SerializedName("collectionURI")
        val collectionURI: String,
        @SerializedName("items")
        val items: List<Item>
    )

    data class SeriesList(
        @SerializedName("available")
        val available: Int,
        @SerializedName("collectionURI")
        val collectionURI: String,
        @SerializedName("items")
        val items: List<Item>
    )

    data class EventList(
        @SerializedName("available")
        val available: Int,
        @SerializedName("collectionURI")
        val collectionURI: String,
        @SerializedName("items")
        val items: List<Item>
    )

    data class Item(
        @SerializedName("resourceURI")
        val resourceURI: String,
        @SerializedName("name")
        val name: String
    )
}