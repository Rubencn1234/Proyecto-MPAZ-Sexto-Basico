package com.example.proyectoappsmobiles.data.model

enum class UserRole(val displayName: String) {
    UTP("Jefatura UTP"),
    DOCENTE("Docente"),
    ESTUDIANTE("Estudiante (6.° Básico)")
}

enum class ContentStatus(val displayName: String) {
    BORRADOR("Borrador"),
    EN_REVISION("En Revisión"),
    AUTORIZADO("Autorizado"),
    PUBLICADO("Publicado")
}

enum class ContentOrigin(val displayName: String) {
    PROPIO("Propio"),
    PUBLICO("Público Mineduc"),
    AUTORIZADO("Autorizado")
}

enum class ReportStatus(val displayName: String) {
    GENERADO("Generado"),
    PENDIENTE_VALIDACION("Pendiente de Validación"),
    VALIDADO("Validado")
}

enum class SubjectType(val title: String, val code: String) {
    LENGUAJE("Lenguaje y Comunicación", "LENG"),
    MATEMATICA("Matemática", "MAT"),
    CIENCIAS_NATURALES("Ciencias Naturales", "CNAT"),
    HISTORIA("Historia, Geografía y Cs. Sociales", "HIST")
}
