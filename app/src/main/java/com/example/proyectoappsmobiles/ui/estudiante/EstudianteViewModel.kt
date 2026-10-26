package com.example.proyectoappsmobiles.ui.estudiante

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoappsmobiles.data.local.entity.ActivityQuestionEntity
import com.example.proyectoappsmobiles.data.local.entity.ContentEntity
import com.example.proyectoappsmobiles.data.local.entity.OaEntity
import com.example.proyectoappsmobiles.data.local.entity.StudentProgressEntity
import com.example.proyectoappsmobiles.data.local.entity.SubjectEntity
import com.example.proyectoappsmobiles.data.local.entity.TeacherConfigEntity
import com.example.proyectoappsmobiles.data.local.entity.UnitEntity
import com.example.proyectoappsmobiles.data.local.entity.UserEntity
import com.example.proyectoappsmobiles.data.model.ContentStatus
import com.example.proyectoappsmobiles.data.repository.AppRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EstudianteViewModel(private val repository: AppRepository) : ViewModel() {

    private val _studentUser = MutableStateFlow<UserEntity?>(null)
    val studentUser: StateFlow<UserEntity?> = _studentUser.asStateFlow()

    private val _subjects = MutableStateFlow<List<SubjectEntity>>(emptyList())
    val subjects: StateFlow<List<SubjectEntity>> = _subjects.asStateFlow()

    private val _selectedSubject = MutableStateFlow<SubjectEntity?>(null)
    val selectedSubject: StateFlow<SubjectEntity?> = _selectedSubject.asStateFlow()

    private val _units = MutableStateFlow<List<UnitEntity>>(emptyList())
    val units: StateFlow<List<UnitEntity>> = _units.asStateFlow()

    private val _oas = MutableStateFlow<List<OaEntity>>(emptyList())
    val oas: StateFlow<List<OaEntity>> = _oas.asStateFlow()

    private val _contents = MutableStateFlow<List<ContentEntity>>(emptyList())
    val contents: StateFlow<List<ContentEntity>> = _contents.asStateFlow()

    private val _practiceQuestions = MutableStateFlow<List<ActivityQuestionEntity>>(emptyList())
    val practiceQuestions: StateFlow<List<ActivityQuestionEntity>> = _practiceQuestions.asStateFlow()

    private val _finalQuestions = MutableStateFlow<List<ActivityQuestionEntity>>(emptyList())
    val finalQuestions: StateFlow<List<ActivityQuestionEntity>> = _finalQuestions.asStateFlow()

    private val _teacherConfig = MutableStateFlow<TeacherConfigEntity?>(null)
    val teacherConfig: StateFlow<TeacherConfigEntity?> = _teacherConfig.asStateFlow()

    private val _progress = MutableStateFlow<StudentProgressEntity?>(null)
    val progress: StateFlow<StudentProgressEntity?> = _progress.asStateFlow()

    fun loadStudent(studentId: String) {
        viewModelScope.launch {
            val user = repository.getUserById(studentId)
            _studentUser.value = user
        }
        viewModelScope.launch {
            repository.getAllSubjects().collect { list ->
                _subjects.value = list
            }
        }
    }

    fun selectSubject(subject: SubjectEntity) {
        _selectedSubject.value = subject
        viewModelScope.launch {
            repository.getUnitsForSubject(subject.id).collect { unitList ->
                _units.value = unitList
                val firstUnit = unitList.firstOrNull()
                if (firstUnit != null) {
                    loadOasForUnit(firstUnit.id)
                }
            }
        }
    }

    fun loadOasForUnit(unitId: String) {
        viewModelScope.launch {
            repository.getOasForUnit(unitId).collect { oaList ->
                _oas.value = oaList
                val firstOa = oaList.firstOrNull()
                if (firstOa != null) {
                    loadOaDetails(firstOa.id)
                }
            }
        }
    }

    fun loadOaDetails(oaId: String) {
        val studentId = _studentUser.value?.id ?: "est_01"

        viewModelScope.launch {
            repository.getContentsForOa(oaId).collect { contentList ->
                // Filter only Published contents for students
                _contents.value = contentList.filter { it.status == ContentStatus.PUBLICADO }
            }
        }
        viewModelScope.launch {
            repository.getPracticeQuestionsForOa(oaId).collect { qList ->
                _practiceQuestions.value = qList
            }
        }
        viewModelScope.launch {
            repository.getFinalTestQuestionsForOa(oaId).collect { qList ->
                _finalQuestions.value = qList
            }
        }
        viewModelScope.launch {
            repository.getTeacherConfigForOa(oaId).collect { cfg ->
                _teacherConfig.value = cfg
            }
        }
        viewModelScope.launch {
            repository.getStudentProgress(studentId, oaId).collect { prog ->
                _progress.value = prog
            }
        }
    }

    fun submitFinalTest(
        oaId: String,
        selectedAnswers: Map<String, Int>,
        questions: List<ActivityQuestionEntity>,
        onComplete: (StudentProgressEntity) -> Unit
    ) {
        val student = _studentUser.value ?: return
        viewModelScope.launch {
            val updated = repository.recordFinalTestRun(
                student = student,
                oaId = oaId,
                answers = selectedAnswers,
                questions = questions
            )
            _progress.value = updated
            onComplete(updated)
        }
    }
}
