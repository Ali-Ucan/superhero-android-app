package com.example.superkahramanlarmanuel.model

fun CharacterResponse.Character.toKahramanlar(isFavorite: Boolean): Kahramanlar {
    val thumbnailUrl = "${thumbnail.path}/portrait_uncanny.${thumbnail.extension}"
    return Kahramanlar(
        id = this.id,
        name = this.name,
        description = this.description,
        thumbnailUrl = thumbnailUrl,
        isFavorite = isFavorite
    )
}

fun CharacterResponse.Thumbnail.toAppThumbnail(): com.example.superkahramanlarmanuel.model.Thumbnail {
    return com.example.superkahramanlarmanuel.model.Thumbnail(
        path = this.path,
        extension = this.extension
    )
}