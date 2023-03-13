package com.example.flirtcompose.model

import kotlinx.serialization.SerialName


data class Person(
    val login: String = "",
    val name: String = "",
    val nick: String = "",
    val iurl: String = "",
    @SerialName("iurl_600")
    val iurl_600: String = "",
    @SerialName("iurl_200")
    val iurl_200: String = "",
    val photos: List<PersonPhoto> = emptyList(),
    val online: Boolean = false,
    val pcnt: Int = 0,
    val age: String = "",
    val city: String = "",
    val aim: String = "",
    @SerialName("photo_id")
    val photoId: Int = 0,
    val moderator: Boolean = false,
    val status: String = "",
    val greeting: String = "",
    val lastVisit: String = "",
    val sex: Int = 0,
)
