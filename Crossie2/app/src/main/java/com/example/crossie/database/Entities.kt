package com.example.crossie.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.crossie.domain.Clue

@Entity(tableName = "clues")
data class ClueTable(
    @PrimaryKey(autoGenerate = false)
    val clue: String,
    val answer: String,
    val hint: String,
    var solved: Int = 0
)

/*
@Entity(tableName = "userlog")
data class UserName(
    @PrimaryKey(autoGenerate = false)
    val userName: String,
    val firstTime: Boolean = true
)
*/


fun List<ClueTable>.asDomainModel(): List<Clue> {
    return map {
        Clue(
            clue = it.clue,
            answer = it.answer,
            hint = it.hint,
            solved = it.solved
        )
    }
}