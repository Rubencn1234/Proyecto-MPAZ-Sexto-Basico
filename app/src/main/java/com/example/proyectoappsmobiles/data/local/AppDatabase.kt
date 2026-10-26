package com.example.proyectoappsmobiles.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.proyectoappsmobiles.data.local.dao.ActivityDao
import com.example.proyectoappsmobiles.data.local.dao.ContentDao
import com.example.proyectoappsmobiles.data.local.dao.CurriculumDao
import com.example.proyectoappsmobiles.data.local.dao.ProgressDao
import com.example.proyectoappsmobiles.data.local.dao.ReportDao
import com.example.proyectoappsmobiles.data.local.dao.UserDao
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        UserEntity::class,
        SubjectEntity::class,
        UnitEntity::class,
        OaEntity::class,
        ContentEntity::class,
        ActivityQuestionEntity::class,
        TeacherConfigEntity::class,
        StudentProgressEntity::class,
        QuestionAttemptEntity::class,
        PedagogicalReportEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun curriculumDao(): CurriculumDao
    abstract fun contentDao(): ContentDao
    abstract fun activityDao(): ActivityDao
    abstract fun progressDao(): ProgressDao
    abstract fun reportDao(): ReportDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                var tempInstance: AppDatabase? = null
                val builder = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "mpaz_pro_6_database.db"
                )
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            CoroutineScope(Dispatchers.IO).launch {
                                tempInstance?.let { populateSeedData(it) }
                            }
                        }
                    })
                    .fallbackToDestructiveMigration()
                
                val instance = builder.build()
                tempInstance = instance
                INSTANCE = instance
                instance
            }
        }

        private suspend fun populateSeedData(db: AppDatabase) {
            try {
                db.userDao().insertUsers(SeedData.users)
                db.curriculumDao().insertSubjects(SeedData.subjects)
                db.curriculumDao().insertUnits(SeedData.units)
                db.curriculumDao().insertOas(SeedData.oas)
                db.contentDao().insertContents(SeedData.contents)
                db.activityDao().insertQuestions(SeedData.questions)
                db.activityDao().insertTeacherConfigs(SeedData.teacherConfigs)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
