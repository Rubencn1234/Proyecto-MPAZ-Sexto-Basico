package com.example.proyectoappsmobiles.ui.utp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoappsmobiles.data.local.entity.ContentEntity
import com.example.proyectoappsmobiles.data.local.entity.OaEntity
import com.example.proyectoappsmobiles.data.model.ContentStatus
import com.example.proyectoappsmobiles.data.repository.AppRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UtpViewModel(private val repository: AppRepository) : ViewModel() {

    private val _contents = MutableStateFlow<List<ContentEntity>>(emptyList())
    val contents: StateFlow<List<ContentEntity>> = _contents.asStateFlow()

    private val _oas = MutableStateFlow<List<OaEntity>>(emptyList())
    val oas: StateFlow<List<OaEntity>> = _oas.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllContents().collect { list ->
                _contents.value = list
            }
        }
        viewModelScope.launch {
            repository.getAllOas().collect { list ->
                _oas.value = list
            }
        }
    }

    fun updateStatus(contentId: String, newStatus: ContentStatus) {
        viewModelScope.launch {
            repository.updateContentStatus(contentId, newStatus)
        }
    }
}
