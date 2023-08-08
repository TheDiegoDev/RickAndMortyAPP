package com.example.rickandmorty.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.rickandmorty.data.database.dao.RickAndMortyDao
import com.example.rickandmorty.data.database.entity.RickAndMortyEntity

@Database(entities = [RickAndMortyEntity::class], version = 1, exportSchema = false)
abstract class RickAndMortyDataBase: RoomDatabase() {

    abstract fun getOompaLoompaDao(): RickAndMortyDao

}