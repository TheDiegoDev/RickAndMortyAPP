package com.example.rickandmorty.data.repository

import com.example.rickandmorty.data.database.RickAndMortyDataBase
import com.example.rickandmorty.data.database.entity.RickAndMortyEntity
import com.example.rickandmorty.data.network.RickAndMortyApi
import com.example.rickandmorty.model.ResponseModel
import com.example.rickandmorty.model.CharactersModel
import com.example.rickandmorty.model.ListResponseModel

class CharacterRepository(
    private val api: RickAndMortyApi,
    private val rickAndMortyDataBase: RickAndMortyDataBase
) {

    suspend fun getCharacters(numPage: String): Result<ListResponseModel> {
        return try {
            val listCharacters = api.getCharacters(numPage)
            addUsers(listCharacters.results)
            Result.success(listCharacters)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCharacterByID(id: String): Result<CharactersModel> {
        return try {
            val character = api.getCharacterByID(id)
            Result.success(character)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getDataByDataBase(): List<CharactersModel> {
        val data = rickAndMortyDataBase.getOompaLoompaDao().getAllDataDao()
         return data.map {
            CharactersModel(
                it.id,
                it.name,
                it.status,
                it.species,
                it.type,
                it.gender,
                it.image,
                it.url,
                it.created
            )
        }
    }

    private suspend fun addUsers(listCharacters: List<CharactersModel>?) {
         try {
             val daoModel = listCharacters?.map {
                 RickAndMortyEntity(
                     it.id,
                     it.name,
                     it.status,
                     it.species,
                     it.type,
                     it.gender,
                     it.image,
                     it.url,
                     it.created
                 )
             }
             daoModel?.let { rickAndMortyDataBase.getOompaLoompaDao().insertAll(it) }
        } catch (_: Exception) { }
    }

    suspend fun getCharacterByDataBase(id: String) : CharactersModel? {
        val character = rickAndMortyDataBase.getOompaLoompaDao().getCharacterDaoByID(id)
        val characterModel = character?.let {
            CharactersModel(
                it.id,
                it.name,
                it.status,
                it.species,
                it.type,
                it.gender,
                it.image,
                it.url,
                it.created
            )
        }
        return characterModel
    }
}






