package com.example.crossie.ui.main

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.*
import com.example.crossie.cluerepo.CluesRepository
import com.example.crossie.database.ClueDatabase
import kotlinx.coroutines.*
import java.lang.Exception

class MainViewModel(application: Application) : AndroidViewModel(application) {
    // TODO: Implement the ViewModel

    private val viewModelJob = SupervisorJob()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val database = ClueDatabase.getDatabase(application)
    val clueRepo = CluesRepository(database)
    private val _cluesFound = MutableLiveData<String>()
    val cluesFound: LiveData<String>
        get() = _cluesFound

    init {
        loadCluesFromWeb()
    }

    private fun loadCluesFromWeb() = coroutineScope.launch {
        try {
            clueRepo.refreshClues()
        } catch (e: Exception) {
            _cluesFound.value = "Check your network connection :)"

        }

    }


    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
