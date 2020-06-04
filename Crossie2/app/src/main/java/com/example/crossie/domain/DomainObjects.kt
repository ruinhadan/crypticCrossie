package com.example.crossie.domain

data class Clue(
    val clue: String,
    val answer: String,
    val hint: String,
    var solved: Int
)