package com.example.rickandmorty.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.data.repository.CharacterRepository
import com.example.rickandmorty.model.ResponseModel
import com.example.rickandmorty.model.CharactersModel
import com.example.rickandmorty.model.ListResponseModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: CharacterRepository
): ViewModel() {
    private var currentPage: Int = 1
    private var moreItems: Boolean = true
    private val _result: MutableStateFlow<ListResponseModel> = MutableStateFlow(ListResponseModel())
    val result = _result.asStateFlow()

    private val _resultDao: MutableStateFlow<List<CharactersModel>> = MutableStateFlow(listOf(
        CharactersModel()
    ))
    val resultDao = _resultDao.asStateFlow()

    private val _resultError: MutableStateFlow<Throwable> = MutableStateFlow(Throwable())
    val resultError = _resultError.asStateFlow()

    fun getCharacters() {
        viewModelScope.launch {
            repository.getCharacters(currentPage.toString())
                .onSuccess {
                    _result.value = it
                    it.info?.let { infoModel ->
                        extractPageValue(infoModel.next)?.let {num ->
                            currentPage = num
                        }
                    }
                    getAllCharacters()
                }
                .onFailure {
                    _resultError.value = it
                }

        }
    }

    private fun getAllCharacters() {
        viewModelScope.launch {
            while (moreItems) {
                repository.getCharacters(currentPage.toString())
                    .onSuccess {
                        _result.value = it
                        it.info?.let { infoModel ->
                            if (currentPage == infoModel.pages) {
                                moreItems = false
                            }
                            extractPageValue(infoModel.next)?.let {num ->
                                currentPage = num
                            }
                        }
                    }
                    .onFailure {
                        _resultError.value = it
                    }
            }
        }
    }

    private fun extractPageValue(input: String?): Int? {
        val keyword = "page="
        val startIndex = input?.indexOf(keyword)
        return if (startIndex != -1) {
            startIndex?.let {
                val pageSubstring = input.substring(it + keyword.length)
                pageSubstring.toIntOrNull()
            }
        } else {
            null
        }
    }

    fun getCharacterDao() {
        viewModelScope.launch {
            _resultDao.value = repository.getDataByDataBase()
        }
    }
}


