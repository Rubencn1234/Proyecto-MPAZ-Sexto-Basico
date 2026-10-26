package com.example.proyectoappsmobiles.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
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
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: String): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<UserEntity>)
}

@Dao
interface CurriculumDao {
    @Query("SELECT * FROM subjects")
    fun getAllSubjects(): Flow<List<SubjectEntity>>

    @Query("SELECT * FROM units WHERE subjectId = :subjectId")
    fun getUnitsForSubject(subjectId: String): Flow<List<UnitEntity>>

    @Query("SELECT * FROM oas WHERE unitId = :unitId")
    fun getOasForUnit(unitId: String): Flow<List<OaEntity>>

    @Query("SELECT * FROM oas WHERE id = :oaId")
    suspend fun getOaById(oaId: String): OaEntity?

    @Query("SELECT * FROM oas")
    fun getAllOas(): Flow<List<OaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubjects(subjects: List<SubjectEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUnits(units: List<UnitEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOas(oas: List<OaEntity>)
}

@Dao
interface ContentDao {
    @Query("SELECT * FROM contents")
    fun getAllContents(): Flow<List<ContentEntity>>

    @Query("SELECT * FROM contents WHERE oaId = :oaId")
    fun getContentsForOa(oaId: String): Flow<List<ContentEntity>>

    @Query("SELECT * FROM contents WHERE id = :contentId")
    suspend fun getContentById(contentId: String): ContentEntity?

    @Query("UPDATE contents SET status = :newStatus WHERE id = :contentId")
    suspend fun updateContentStatus(contentId: String, newStatus: ContentStatus)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContents(contents: List<ContentEntity>)
}

@Dao
interface ActivityDao {
    @Query("SELECT * FROM activity_questions WHERE oaId = :oaId AND isFinalTest = 0")
    fun getPracticeQuestionsForOa(oaId: String): Flow<List<ActivityQuestionEntity>>

    @Query("SELECT * FROM activity_questions WHERE oaId = :oaId AND isFinalTest = 1")
    fun getFinalTestQuestionsForOa(oaId: String): Flow<List<ActivityQuestionEntity>>

    @Query("SELECT * FROM teacher_configs WHERE oaId = :oaId")
    fun getTeacherConfigForOa(oaId: String): Flow<TeacherConfigEntity?>

    @Query("SELECT * FROM teacher_configs WHERE oaId = :oaId")
    suspend fun getTeacherConfigDirect(oaId: String): TeacherConfigEntity?

    @Query("SELECT * FROM teacher_configs")
    fun getAllTeacherConfigs(): Flow<List<TeacherConfigEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateTeacherConfig(config: TeacherConfigEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<ActivityQuestionEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeacherConfigs(configs: List<TeacherConfigEntity>)
}

@Dao
interface ProgressDao {
    @Query("SELECT * FROM student_progress WHERE studentId = :studentId AND oaId = :oaId")
    fun getProgress(studentId: String, oaId: String): Flow<StudentProgressEntity?>

    @Query("SELECT * FROM student_progress WHERE studentId = :studentId AND oaId = :oaId")
    suspend fun getProgressDirect(studentId: String, oaId: String): StudentProgressEntity?

    @Query("SELECT * FROM student_progress WHERE studentId = :studentId")
    fun getAllProgressForStudent(studentId: String): Flow<List<StudentProgressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProgress(progress: StudentProgressEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun recordAttempt(attempt: QuestionAttemptEntity)

    @Query("SELECT * FROM question_attempts WHERE studentId = :studentId AND oaId = :oaId AND attemptNumber = :attemptNumber")
    suspend fun getAttemptsForRun(studentId: String, oaId: String, attemptNumber: Int): List<QuestionAttemptEntity>
}

@Dao
interface ReportDao {
    @Query("SELECT * FROM pedagogical_reports")
    fun getAllReports(): Flow<List<PedagogicalReportEntity>>

    @Query("SELECT * FROM pedagogical_reports WHERE studentFictionalName = :studentName OR 'ALL' = :studentName")
    fun getReportsForStudent(studentName: String): Flow<List<PedagogicalReportEntity>>

    @Query("SELECT * FROM pedagogical_reports WHERE id = :reportId")
    suspend fun getReportById(reportId: String): PedagogicalReportEntity?

    @Query("UPDATE pedagogical_reports SET status = :status, teacherResponsible = :teacherName WHERE id = :reportId")
    suspend fun updateReportStatus(reportId: String, status: ReportStatus, teacherName: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReport(report: PedagogicalReportEntity)
}
