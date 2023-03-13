package com.example.flirtcompose.model

data class PersonPhoto(
    val error: String = "",
    var url: String = "",
    var urlBig: String = "",
    val photo_id: Int = 0,
    val liked: String = " ",
    val likedCount: String = "",
    val descr: String = "",
    val status: String = "",
)
