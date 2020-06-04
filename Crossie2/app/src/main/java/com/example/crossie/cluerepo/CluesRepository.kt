package com.example.crossie.cluerepo

import android.graphics.DiscretePathEffect
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.crossie.database.ClueDatabase
import com.example.crossie.database.asDomainModel
import com.example.crossie.domain.Clue
import com.example.crossie.network.ClueNetwork
import com.example.crossie.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CluesRepository(private val clueDatabase: ClueDatabase) {

    suspend fun refreshClues() {
        withContext(Dispatchers.IO) {
            Log.i("CluesRepository","Refreshing Clues")
            val clueList = ClueNetwork.clues.getClues().await()
            Log.i("CluesRepository","${clueList.clues.size}")
            clueDatabase.clueDao.insertAll(clueList.clues.asDatabaseModel())
        }
    }
}