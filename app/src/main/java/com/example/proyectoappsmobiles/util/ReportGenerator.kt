package com.example.proyectoappsmobiles.util

import com.example.proyectoappsmobiles.data.local.entity.OaEntity
import com.example.proyectoappsmobiles.data.local.entity.PedagogicalReportEntity
import com.example.proyectoappsmobiles.data.local.entity.SubjectEntity
import com.example.proyectoappsmobiles.data.local.entity.UnitEntity
import com.example.proyectoappsmobiles.data.local.entity.UserEntity
import com.example.proyectoappsmobiles.data.model.ReportStatus
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ReportGenerator {

    fun generateReport(
        student: UserEntity,
        subject: SubjectEntity,
        unit: UnitEntity,
        oa: OaEntity,
        correctCount: Int,
        totalCount: Int
    ): PedagogicalReportEntity {
        val scorePercentage = if (totalCount > 0) (correctCount * 100) / totalCount else 0
        val isMastered = scorePercentage >= 70

        val strengths = if (isMastered) {
            "El estudiante demuestra un nivel satisfactorio de logro en el ${oa.code} (${oa.axis}). Comprende los conceptos clave de ${subject.title} y resuelve adecuadamente los ítems de evaluación."
        } else {
            "El estudiante identifica parcialmente algunos elementos iniciales del ${oa.code} (${oa.axis}), mostrando disposición durante la ejecución de las actividades."
        }

        val reinforcement = if (isMastered) {
            "Se sugiere continuar con desafíos de mayor complejidad y apoyar a pares en actividades colaborativas."
        } else {
            "Se recomienda reexaminar la explicación conceptual, realizar actividades guiadas adicionales y solicitar mediación pedagógica docente en ${subject.title}."
        }

        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val formattedDate = dateFormat.format(Date())

        return PedagogicalReportEntity(
            id = "RPT_${student.id}_${oa.id}",
            studentFictionalName = student.name,
            courseName = student.courseName,
            subjectTitle = subject.title,
            unitTitle = unit.title,
            oaCode = oa.code,
            scorePercentage = scorePercentage,
            correctCount = correctCount,
            totalCount = totalCount,
            strengthsSummary = strengths,
            reinforcementSummary = reinforcement,
            dateFormatted = formattedDate,
            teacherResponsible = "Pendiente de Asignación",
            status = ReportStatus.PENDIENTE_VALIDACION
        )
    }
}
