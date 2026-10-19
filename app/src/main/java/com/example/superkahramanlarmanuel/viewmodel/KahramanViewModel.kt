package com.example.superkahramanlarmanuel.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.superkahramanlarmanuel.model.Kahramanlar
import com.example.superkahramanlarmanuel.repository.MarvelRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class KahramanViewModel(private val repository: MarvelRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _selectedCharacter = MutableStateFlow<Kahramanlar?>(null)
    val selectedCharacter: StateFlow<Kahramanlar?> = _selectedCharacter.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    init {
        if (_uiState.value is UiState.Loading) {
            getCharacters()
        }
    }

    fun getCharacters() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val results = repository.getCharacters()
                if (results.isNotEmpty()) {
                    _uiState.value = UiState.Success(results)
                } else {
                    _uiState.value = UiState.Empty
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Karakterler yüklenemedi: ${e.message}")
            }
        }
    }

    fun searchCharacters(query: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val results = if (query.isEmpty()) {
                    repository.getCharacters()
                } else {
                    repository.searchCharacters(query)
                }

                if (results.isNotEmpty()) {
                    _uiState.value = UiState.Success(results)
                } else {
                    _uiState.value = UiState.Empty
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Karakterler yüklenemedi: ${e.message}")
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        if (query.length > 2 || query.isEmpty()) {
            searchCharacters(query)
        }
    }

    sealed class UiState {
        object Loading : UiState()
        data class Success(val characters: List<Kahramanlar>) : UiState()
        data class Error(val message: String) : UiState()
        object Empty : UiState()
    }

    fun getCharacterById(characterId: Int) {
        viewModelScope.launch {
            _selectedCharacter.value = null
            try {
                val character = repository.getCharacterById(characterId)
                _selectedCharacter.value = character
            } catch (e: Exception) {
                _selectedCharacter.value = null
            }
        }
    }

    fun toggleFavorite(kahraman: Kahramanlar) {
        val updatedCharacter = repository.toggleFavorite(kahraman)
        _selectedCharacter.value = updatedCharacter.copy()

            Log.d("ViewModel", "Favori durumu değişti: ${updatedCharacter.name} -> ${updatedCharacter.isFavorite}")

        }
    }
