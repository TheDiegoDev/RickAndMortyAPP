package com.example.rickandmorty.data.network

import com.example.rickandmorty.model.ResponseModel
import com.example.rickandmorty.model.CharactersModel
import com.example.rickandmorty.model.ListResponseModel
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickAndMortyApi {
    companion object {
        val instance: RickAndMortyApi = Retrofit.Builder()
            .baseUrl("https://rickandmortyapi.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().build()
            ).build().create(RickAndMortyApi::class.java)
    }

    @GET("character")
    suspend fun getCharacters(@Query("page") page: String): ListResponseModel

    @GET("character/{id}")
    suspend fun getCharacterByID(@Path("id") id: String): CharactersModel
}