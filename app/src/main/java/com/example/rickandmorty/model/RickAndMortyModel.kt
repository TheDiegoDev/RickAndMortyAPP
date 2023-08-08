package com.example.rickandmorty.model

import androidx.room.ColumnInfo

data class CharactersModel(
    var id: Int? = null,
    var name: String? = null,
    var status: String? = null,
    var species: String? = null,
    var type: String? = null,
    var gender: String? = null,
    var image: String? = null,
    var url: String? = null,
    var created: String? = null
)

class ResponseModel(
    var count: Int? = null,
    var pages: Int? = null,
    var next: String? = null,
    var prev: String? = null,
)

class ListResponseModel(
    var info: ResponseModel? = null,
    var results: List<CharactersModel>? = null
)