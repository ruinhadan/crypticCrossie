package com.example.crossie.game

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.example.crossie.cluerepo.CluesRepository
import com.example.crossie.database.ClueDatabase
import com.example.crossie.database.ClueTable
import com.example.crossie.database.asDomainModel
import com.example.crossie.domain.Clue
import com.example.crossie.network.ClueNetwork
import com.example.crossie.network.ClueService
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import java.lang.Exception

/*
data class Clue(
    val clue: String,
    val answer: String,
    val hint: String,
    var solved: Boolean
)

val clueList = listOf<Clue>(
    Clue("Knock harbor with affinity(7)", "rapport", "Charade", false),
    Clue("Endless church with headless old women as caretakers(10)", "chaperones", "Deletion", false)
)
*/

class GameViewModel(application: Application) : ViewModel() {
    private val _nextButtonEnabled = MutableLiveData<Boolean>()
    val nextButtonEnabled: LiveData<Boolean>
        get() = _nextButtonEnabled

    private val _prevButtonEnabled = MutableLiveData<Boolean>()
    val prevButtonEnabled: LiveData<Boolean>
        get() = _prevButtonEnabled

    private val _currentClue = MutableLiveData<Clue>()
    val currentClue: LiveData<Clue>
        get() = _currentClue

    private val _hintVisible = MutableLiveData<Boolean>()
    val hintVisible: LiveData<Boolean>
        get() = _hintVisible

    private val _solved = MutableLiveData<Boolean?>()
    val solved: LiveData<Boolean?>
        get() = _solved

    private val _cluesFound = MutableLiveData<String>()
    val cluesFound: LiveData<String>
        get() = _cluesFound

    private val viewModelJob = SupervisorJob()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private val database = ClueDatabase.getDatabase(application)
    private lateinit var clueList: List<Clue>

    private var currentIndex = 0
    var totalClues = 0

    init {
        loadFromDB()
    }

    fun loadFromDB() = coroutineScope.launch{
        loadFromDatabase()
        totalClues = clueList.size
        displayCurrentClue()
    }

    suspend fun loadFromDatabase(){
        withContext(Dispatchers.IO) {
            clueList = database.clueDao.getUnsolvedClues().asDomainModel()
        }
    }

    fun giveHint() {
        _hintVisible.value = true
        _cluesFound.value = "Found ${clueList.size} clues"
    }

    fun nextClue() {
        if(currentIndex < totalClues-1)
            currentIndex++
        displayCurrentClue()
    }

    fun prevClue() {
        if(currentIndex > 0)
            currentIndex--
        displayCurrentClue()
    }

    fun displayCurrentClue() {
        if(clueList.isNullOrEmpty()) {
            _cluesFound.value = "No new clues found :/"
            totalClues = 0
            _currentClue.value = Clue(clue = "All Clues Solved!",answer = "Congratulations", hint = "Check back later to see if new clues are available :)", solved = 1)
        } else {
            _currentClue.value = clueList[currentIndex]
            totalClues = clueList.size
        }
        _solved.value = when(currentClue.value?.solved) {
            1 -> true
            else -> null
        }
        _hintVisible.value = false
        _nextButtonEnabled.value = (currentIndex < totalClues-1)
        _prevButtonEnabled.value = (currentIndex > 0)
    }

    fun checkAnswer(answer: String) = coroutineScope.launch {
        if (answer.toLowerCase().equals(currentClue.value?.answer)) {
            currentClue.value?.solved = 1
            _solved.value = true
            updateCorrectAnswer(currentClue.value!!.clue)
        } else _solved.value = false
    }


    suspend fun updateCorrectAnswer(clue: String) {
        withContext(Dispatchers.IO){
            database.clueDao.updateSolved(clue)
        }
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return GameViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
