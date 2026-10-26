package com.example.proyectoappsmobiles.ui.docente

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoappsmobiles.data.local.entity.OaEntity
import com.example.proyectoappsmobiles.data.local.entity.PedagogicalReportEntity
import com.example.proyectoappsmobiles.data.local.entity.TeacherConfigEntity
import com.example.proyectoappsmobiles.data.repository.AppRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DocenteViewModel(private val repository: AppRepository) : ViewModel() {

    private val _oas = MutableStateFlow<List<OaEntity>>(emptyList())
    val oas: StateFlow<List<OaEntity>> = _oas.asStateFlow()

    private val _teacherConfigs = MutableStateFlow<List<TeacherConfigEntity>>(emptyList())
    val teacherConfigs: StateFlow<List<TeacherConfigEntity>> = _teacherConfigs.asStateFlow()

    private val _reports = MutableStateFlow<List<PedagogicalReportEntity>>(emptyList())
    val reports: StateFlow<List<PedagogicalReportEntity>> = _reports.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllOas().collect { list ->
                _oas.value = list
            }
        }
        viewModelScope.launch {
            repository.getAllTeacherConfigs().collect { list ->
                _teacherConfigs.value = list
            }
        }
        viewModelScope.launch {
            repository.getAllReports().collect { list ->
                _reports.value = list
            }
        }
    }

    fun updateMaxAttempts(oaId: String, newAttempts: Int) {
        viewModelScope.launch {
            val current = _teacherConfigs.value.find { it.oaId == oaId }
                ?: TeacherConfigEntity(oaId = oaId, maxAttemptsAllowed = newAttempts)
            repository.updateTeacherConfig(current.copy(maxAttemptsAllowed = newAttempts))
        }
    }

    fun toggleAssignment(oaId: String, currentAssigned: Boolean) {
        viewModelScope.launch {
            val current = _teacherConfigs.value.find { it.oaId == oaId }
                ?: TeacherConfigEntity(oaId = oaId, isAssignedToStudents = !currentAssigned)
            repository.updateTeacherConfig(current.copy(isAssignedToStudents = !currentAssigned))
        }
    }

    fun validatePedagogicalReport(reportId: String, teacherName: String) {
        viewModelScope.launch {
            repository.validateReport(reportId, teacherName)
        }
    }
}
