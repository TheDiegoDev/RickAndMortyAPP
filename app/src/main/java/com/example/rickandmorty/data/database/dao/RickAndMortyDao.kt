package com.example.rickandmorty.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.rickandmorty.data.database.entity.RickAndMortyEntity

@Dao
interface RickAndMortyDao {

    @Query("SELECT * FROM rick_and_morty_table")
    suspend fun getAllDataDao(): List<RickAndMortyEntity>

    @Query("SELECT * FROM rick_and_morty_table WHERE id = :id")
    suspend fun getCharacterDaoByID(id: String): RickAndMortyEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(quotes: List<RickAndMortyEntity>)
}