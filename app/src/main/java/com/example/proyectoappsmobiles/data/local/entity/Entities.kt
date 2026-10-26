package com.example.proyectoappsmobiles.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.proyectoappsmobiles.data.model.ContentOrigin
import com.example.proyectoappsmobiles.data.model.ContentStatus
import com.example.proyectoappsmobiles.data.model.ReportStatus
import com.example.proyectoappsmobiles.data.model.SubjectType
import com.example.proyectoappsmobiles.data.model.UserRole

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val name: String,
    val role: UserRole,
    val courseName: String = "6.º Básico A"
)

@Entity(tableName = "subjects")
data class SubjectEntity(
    @PrimaryKey val id: String,
    val type: SubjectType,
    val title: String,
    val iconResName: String
)

@Entity(tableName = "units")
data class UnitEntity(
    @PrimaryKey val id: String,
    val subjectId: String,
    val unitNumber: Int,
    val title: String,
    val description: String
)

@Entity(tableName = "oas")
data class OaEntity(
    @PrimaryKey val id: String,
    val unitId: String,
    val code: String, // ej. "OA 08"
    val axis: String, // ej. "Números y Operaciones"
    val description: String
)

@Entity(tableName = "contents")
data class ContentEntity(
    @PrimaryKey val id: String,
    val oaId: String,
    val title: String,
    val explanation: String,
    val exampleText: String,
    val instructions: String,
    val visualResourceHint: String,
    val origin: ContentOrigin,
    val status: ContentStatus,
    val referenceTextbook: String,
    val hasAudioSupport: Boolean = true
)

@Entity(tableName = "activity_questions")
data class ActivityQuestionEntity(
    @PrimaryKey val id: String,
    val oaId: String,
    val contentId: String,
    val title: String,
    val questionText: String,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    val correctAnswerIndex: Int, // 0..3
    val difficulty: String, // "Fácil", "Medio", "Desafío"
    val isFinalTest: Boolean = false
)

@Entity(tableName = "teacher_configs")
data class TeacherConfigEntity(
    @PrimaryKey val oaId: String,
    val maxAttemptsAllowed: Int = 3,
    val isAssignedToStudents: Boolean = true
)

@Entity(tableName = "student_progress")
data class StudentProgressEntity(
    @PrimaryKey val id: String, // "${studentId}_${oaId}"
    val studentId: String,
    val oaId: String,
    val attemptsUsed: Int = 0,
    val bestScorePercentage: Int = 0,
    val isCompleted: Boolean = false,
    val lastUpdatedTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "question_attempts")
data class QuestionAttemptEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val studentId: String,
    val oaId: String,
    val questionId: String,
    val selectedOptionIndex: Int,
    val isCorrect: Boolean,
    val attemptNumber: Int,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "pedagogical_reports")
data class PedagogicalReportEntity(
    @PrimaryKey val id: String, // "RPT_${studentId}_${oaId}"
    val studentFictionalName: String,
    val courseName: String,
    val subjectTitle: String,
    val unitTitle: String,
    val oaCode: String,
    val scorePercentage: Int,
    val correctCount: Int,
    val totalCount: Int,
    val strengthsSummary: String,
    val reinforcementSummary: String,
    val dateFormatted: String,
    val teacherResponsible: String,
    val status: ReportStatus = ReportStatus.PENDIENTE_VALIDACION
)
