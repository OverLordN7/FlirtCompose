package com.example.flirtcompose.model

data class Request(
    val searchSessionId: String,
    val cnt: String,
    val users: List<Person>,
    val total: Int,
    val error: String,
    val status: String
)