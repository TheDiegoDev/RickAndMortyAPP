package com.example.rickandmorty.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.data.repository.CharacterRepository
import com.example.rickandmorty.model.CharactersModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailActivityViewModel(private val repository: CharacterRepository
): ViewModel() {
    private val _result: MutableStateFlow<CharactersModel> = MutableStateFlow(CharactersModel())
    val result = _result.asStateFlow()

    private val _resultError: MutableStateFlow<Throwable> = MutableStateFlow(Throwable())
    val resultError = _resultError.asStateFlow()

    private fun getCharacterById(id: String) {
        viewModelScope.launch {
            repository.getCharacterByID(id)
                .onSuccess {
                    _result.value = it
                }
                .onFailure {
                    _resultError.value = it
                }
        }
    }

    fun getCharacterDataBase(id: String) {
        viewModelScope.launch {
            if (repository.getCharacterByDataBase(id) != null) {
                repository.getCharacterByDataBase(id)?.let {
                    _result.value = it
                }
            } else {
                getCharacterById(id)
            }
        }
    }
}