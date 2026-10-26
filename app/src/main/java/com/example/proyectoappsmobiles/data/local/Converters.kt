package com.example.proyectoappsmobiles.data.local

import androidx.room.TypeConverter
import com.example.proyectoappsmobiles.data.model.ContentOrigin
import com.example.proyectoappsmobiles.data.model.ContentStatus
import com.example.proyectoappsmobiles.data.model.ReportStatus
import com.example.proyectoappsmobiles.data.model.SubjectType
import com.example.proyectoappsmobiles.data.model.UserRole

class Converters {
    @TypeConverter
    fun fromUserRole(value: UserRole): String = value.name

    @TypeConverter
    fun toUserRole(value: String): UserRole = enumValueOf(value)

    @TypeConverter
    fun fromContentStatus(value: ContentStatus): String = value.name

    @TypeConverter
    fun toContentStatus(value: String): ContentStatus = enumValueOf(value)

    @TypeConverter
    fun fromContentOrigin(value: ContentOrigin): String = value.name

    @TypeConverter
    fun toContentOrigin(value: String): ContentOrigin = enumValueOf(value)

    @TypeConverter
    fun fromReportStatus(value: ReportStatus): String = value.name

    @TypeConverter
    fun toReportStatus(value: String): ReportStatus = enumValueOf(value)

    @TypeConverter
    fun fromSubjectType(value: SubjectType): String = value.name

    @TypeConverter
    fun toSubjectType(value: String): SubjectType = enumValueOf(value)
}
