package com.example.crossie.network

import com.example.crossie.database.ClueTable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ClueContainer(val clues: List<NetworkClue>)

@JsonClass(generateAdapter = true)
data class NetworkClue(
    val clue: String,
    val answer: String,
    @Json(name="clue_type") val clueType: String
)

fun List<NetworkClue>.asDatabaseModel(): List<ClueTable>{
    return map{
        ClueTable(
            clue = it.clue,
            answer = it.answer,
            hint = it.clueType,
            solved = 0
        )
    }
}