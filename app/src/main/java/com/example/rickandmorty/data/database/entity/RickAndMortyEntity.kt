package com.example.rickandmorty.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.ClientInfoStatus

@Entity(tableName = "rick_and_morty_table")
data class RickAndMortyEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int?,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "status") val status: String?,
    @ColumnInfo(name = "species") val species: String?,
    @ColumnInfo(name = "type") val type: String?,
    @ColumnInfo(name = "gender") val gender: String?,
    @ColumnInfo(name = "image") val image: String?,
    @ColumnInfo(name = "url") val url: String?,
    @ColumnInfo(name = "created") val created: String?,
)
