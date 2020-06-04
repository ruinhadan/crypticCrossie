package com.example.crossie.network

import kotlinx.coroutines.Deferred
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

interface ClueService {
    @GET("clues.json")
    fun getClues(): Deferred<ClueContainer>
}

object ClueNetwork {
    private val retrofit = Retrofit.Builder().baseUrl("https://ruinhadan.github.io/crypticCrossie/")
        .addConverterFactory(MoshiConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val clues = retrofit.create(ClueService::class.java)
}