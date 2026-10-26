package com.example.proyectoappsmobiles.data.repository

import com.example.proyectoappsmobiles.data.local.AppDatabase
import com.example.proyectoappsmobiles.data.local.entity.ActivityQuestionEntity
import com.example.proyectoappsmobiles.data.local.entity.ContentEntity
import com.example.proyectoappsmobiles.data.local.entity.OaEntity
import com.example.proyectoappsmobiles.data.local.entity.PedagogicalReportEntity
import com.example.proyectoappsmobiles.data.local.entity.QuestionAttemptEntity
import com.example.proyectoappsmobiles.data.local.entity.StudentProgressEntity
import com.example.proyectoappsmobiles.data.local.entity.SubjectEntity
import com.example.proyectoappsmobiles.data.local.entity.TeacherConfigEntity
import com.example.proyectoappsmobiles.data.local.entity.UnitEntity
import com.example.proyectoappsmobiles.data.local.entity.UserEntity
import com.example.proyectoappsmobiles.data.model.ContentStatus
import com.example.proyectoappsmobiles.data.model.ReportStatus
import com.example.proyectoappsmobiles.util.ReportGenerator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class AppRepository(private val db: AppDatabase) {

    // Users
    fun getAllUsers(): Flow<List<UserEntity>> = db.userDao().getAllUsers()
    suspend fun getUserById(userId: String): UserEntity? = db.userDao().getUserById(userId)

    // Curriculum
    fun getAllSubjects(): Flow<List<SubjectEntity>> = db.curriculumDao().getAllSubjects()
    fun getUnitsForSubject(subjectId: String): Flow<List<UnitEntity>> = db.curriculumDao().getUnitsForSubject(subjectId)
    fun getOasForUnit(unitId: String): Flow<List<OaEntity>> = db.curriculumDao().getOasForUnit(unitId)
    fun getAllOas(): Flow<List<OaEntity>> = db.curriculumDao().getAllOas()
    suspend fun getOaById(oaId: String): OaEntity? = db.curriculumDao().getOaById(oaId)

    // Content (UTP & General)
    fun getAllContents(): Flow<List<ContentEntity>> = db.contentDao().getAllContents()
    fun getContentsForOa(oaId: String): Flow<List<ContentEntity>> = db.contentDao().getContentsForOa(oaId)
    suspend fun updateContentStatus(contentId: String, newStatus: ContentStatus) {
        db.contentDao().updateContentStatus(contentId, newStatus)
    }

    // Teacher Config & Questions
    fun getPracticeQuestionsForOa(oaId: String): Flow<List<ActivityQuestionEntity>> = db.activityDao().getPracticeQuestionsForOa(oaId)
    fun getFinalTestQuestionsForOa(oaId: String): Flow<List<ActivityQuestionEntity>> = db.activityDao().getFinalTestQuestionsForOa(oaId)
    fun getTeacherConfigForOa(oaId: String): Flow<TeacherConfigEntity?> = db.activityDao().getTeacherConfigForOa(oaId)
    fun getAllTeacherConfigs(): Flow<List<TeacherConfigEntity>> = db.activityDao().getAllTeacherConfigs()

    suspend fun updateTeacherConfig(config: TeacherConfigEntity) {
        db.activityDao().insertOrUpdateTeacherConfig(config)
    }

    // Student Progress & Attempt Handling
    fun getStudentProgress(studentId: String, oaId: String): Flow<StudentProgressEntity?> = db.progressDao().getProgress(studentId, oaId)

    suspend fun recordFinalTestRun(
        student: UserEntity,
        oaId: String,
        answers: Map<String, Int>, // questionId -> selectedOptionIndex
        questions: List<ActivityQuestionEntity>
    ): StudentProgressEntity {
        val currentProgress = db.progressDao().getProgressDirect(student.id, oaId)
        val attemptNumber = (currentProgress?.attemptsUsed ?: 0) + 1

        var correctCount = 0
        questions.forEach { q ->
            val selected = answers[q.id] ?: -1
            val isCorrect = selected == q.correctAnswerIndex
            if (isCorrect) correctCount++

            db.progressDao().recordAttempt(
                QuestionAttemptEntity(
                    studentId = student.id,
                    oaId = oaId,
                    questionId = q.id,
                    selectedOptionIndex = selected,
                    isCorrect = isCorrect,
                    attemptNumber = attemptNumber
                )
            )
        }

        val totalCount = questions.size
        val scorePct = if (totalCount > 0) (correctCount * 100) / totalCount else 0
        val bestScore = maxOf(currentProgress?.bestScorePercentage ?: 0, scorePct)

        val updatedProgress = StudentProgressEntity(
            id = "${student.id}_$oaId",
            studentId = student.id,
            oaId = oaId,
            attemptsUsed = attemptNumber,
            bestScorePercentage = bestScore,
            isCompleted = true,
            lastUpdatedTimestamp = System.currentTimeMillis()
        )
        db.progressDao().saveProgress(updatedProgress)

        // Generate Report automatically for Teacher Validation
        val oa = db.curriculumDao().getOaById(oaId)
        if (oa != null) {
            val units = db.curriculumDao().getUnitsForSubject(oa.unitId).firstOrNull() ?: emptyList()
            val unit = units.find { it.id == oa.unitId } ?: UnitEntity(oa.unitId, "", 1, "Unidad de Estudio", "")
            val subjects = db.curriculumDao().getAllSubjects().firstOrNull() ?: emptyList()
            val subject = subjects.find { it.id == unit.subjectId } ?: SubjectEntity(unit.subjectId, com.example.proyectoappsmobiles.data.model.SubjectType.LENGUAJE, "Asignatura", "ic_book")

            val report = ReportGenerator.generateReport(
                student = student,
                subject = subject,
                unit = unit,
                oa = oa,
                correctCount = correctCount,
                totalCount = totalCount
            )
            db.reportDao().insertReport(report)
        }

        return updatedProgress
    }

    // Pedagogical Reports (Docente)
    fun getAllReports(): Flow<List<PedagogicalReportEntity>> = db.reportDao().getAllReports()

    suspend fun validateReport(reportId: String, teacherName: String) {
        db.reportDao().updateReportStatus(reportId, ReportStatus.VALIDADO, teacherName)
    }
}
