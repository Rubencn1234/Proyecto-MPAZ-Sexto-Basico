package com.example.proyectoappsmobiles.data.local

import com.example.proyectoappsmobiles.data.local.entity.ActivityQuestionEntity
import com.example.proyectoappsmobiles.data.local.entity.ContentEntity
import com.example.proyectoappsmobiles.data.local.entity.OaEntity
import com.example.proyectoappsmobiles.data.local.entity.SubjectEntity
import com.example.proyectoappsmobiles.data.local.entity.TeacherConfigEntity
import com.example.proyectoappsmobiles.data.local.entity.UnitEntity
import com.example.proyectoappsmobiles.data.local.entity.UserEntity
import com.example.proyectoappsmobiles.data.model.ContentOrigin
import com.example.proyectoappsmobiles.data.model.ContentStatus
import com.example.proyectoappsmobiles.data.model.SubjectType
import com.example.proyectoappsmobiles.data.model.UserRole

object SeedData {

    val users = listOf(
        UserEntity("utp_01", "jefatura@utp", "Camila Soto (Jefatura UTP)", UserRole.UTP, "Colegio MPAZ 6.º"),
        UserEntity("doc_01", "profesor@profe", "Prof. Roberto Gómez", UserRole.DOCENTE, "6.º Básico A"),
        UserEntity("est_01", "estudiante@estudio", "Estudiante Ficticio", UserRole.ESTUDIANTE, "6.º Básico A")
    )

    val subjects = listOf(
        SubjectEntity("sub_leng", SubjectType.LENGUAJE, "Lenguaje y Comunicación", "ic_book"),
        SubjectEntity("sub_mat", SubjectType.MATEMATICA, "Matemática", "ic_calculate"),
        SubjectEntity("sub_cnat", SubjectType.CIENCIAS_NATURALES, "Ciencias Naturales", "ic_science"),
        SubjectEntity("sub_hist", SubjectType.HISTORIA, "Historia, Geografía y Cs. Sociales", "ic_public")
    )

    val units = listOf(
        UnitEntity(
            id = "unit_leng_1",
            subjectId = "sub_leng",
            unitNumber = 1,
            title = "Unidad 1: Comprensión Lectora y Textos Literarios",
            description = "Estrategias de análisis narrativo, conflicto dramático, personajes y ambiente en cuentos y leyendas."
        ),
        UnitEntity(
            id = "unit_mat_1",
            subjectId = "sub_mat",
            unitNumber = 1,
            title = "Unidad 1: Números y Operaciones - Fracciones y Decimales",
            description = "Representación, comparación y conversión entre fracciones impropias y números mixtos."
        ),
        UnitEntity(
            id = "unit_cnat_1",
            subjectId = "sub_cnat",
            unitNumber = 1,
            title = "Unidad 1: Sistemas del Cuerpo Humano y Salud",
            description = "Estructura celular, tejidos, órganos y sistemas biológicos integrados en la nutrición humana."
        ),
        UnitEntity(
            id = "unit_hist_1",
            subjectId = "sub_hist",
            unitNumber = 1,
            title = "Unidad 1: Organización Política y Democracia en Chile",
            description = "Los tres poderes del Estado, la Constitución Política y los derechos y deberes ciudadanos."
        )
    )

    val oas = listOf(
        OaEntity(
            id = "oa_leng_03",
            unitId = "unit_leng_1",
            code = "OA 03",
            axis = "Eje Lectura",
            description = "Analizar las narraciones leídas para enriquecer su comprensión, considerando el conflicto, los personajes y el entorno."
        ),
        OaEntity(
            id = "oa_mat_08",
            unitId = "unit_mat_1",
            code = "OA 08",
            axis = "Eje Números y Operaciones",
            description = "Demostrar que comprenden las fracciones impropias de uso común y los números mixtos asociados de forma pictórica y simbólica."
        ),
        OaEntity(
            id = "oa_cnat_02",
            unitId = "unit_cnat_1",
            code = "OA 02",
            axis = "Eje Ciencias de la Vida",
            description = "Explicar la organización biológica de los seres vivos en niveles: célula, tejido, órgano, sistema y organismo."
        ),
        OaEntity(
            id = "oa_hist_15",
            unitId = "unit_hist_1",
            code = "OA 15",
            axis = "Eje Formación Ciudadana",
            description = "Explicar los elementos fundamentales de la organización democrática en Chile, como la división de poderes del Estado y el rol de la Constitución."
        )
    )

    val contents = listOf(
        // LENGUAJE
        ContentEntity(
            id = "cnt_leng_1",
            oaId = "oa_leng_03",
            title = "El Conflicto Narrativo y la Evolución de los Personajes",
            explanation = "En todo cuento o historia, el conflicto es el problema principal que impulsa la acción de los personajes. Los personajes principales o protagonistas intentan resolverlo, mientras que el ambiente (tiempo y lugar) influye en sus decisiones.",
            exampleText = "Ejemplo: En el cuento 'El faro de la isla', el farero debe mantener encendida la luz durante una tormenta violenta (conflicto principal vs. la naturaleza).",
            instructions = "Lee atentamente la explicación y observa cómo el problema inicial cambia las motivaciones del personaje principal.",
            visualResourceHint = "Diagrama del arco narrativo: Inicio → Conflicto → Clímax → Desenlace",
            origin = ContentOrigin.PROPIO,
            status = ContentStatus.PUBLICADO,
            referenceTextbook = "Texto de Estudio Lenguaje 6.º Básico, Mineduc, pág. 18-24"
        ),
        // MATEMATICA
        ContentEntity(
            id = "cnt_mat_1",
            oaId = "oa_mat_08",
            title = "Fracciones Impropias y Números Mixtos",
            explanation = "Una fracción impropia es aquella donde el numerador es mayor o igual que el denominador (ej. 7/4). Representa más de un entero completo. Un número mixto combina un entero y una fracción propia (ej. 1 3/4). ¡Ambos representan la misma cantidad!",
            exampleText = "Ejemplo: Si tienes 7 cuartos de pizza (7/4), tienes 1 pizza entera (4/4) y 3 cuartos adicionales (3/4). Es decir: 7/4 = 1 3/4.",
            instructions = "Divide el numerador por el denominador para encontrar los enteros completos y el residuo será la nueva fracción.",
            visualResourceHint = "Gráfico circular: 1 círculo completo coloreado + 1 círculo con 3 de 4 partes coloreadas",
            origin = ContentOrigin.PUBLICO,
            status = ContentStatus.PUBLICADO,
            referenceTextbook = "Texto de Estudio Matemática 6.º Básico, Mineduc, pág. 42-48"
        ),
        // CIENCIAS NATURALES
        ContentEntity(
            id = "cnt_cnat_1",
            oaId = "oa_cnat_02",
            title = "Los 5 Niveles de Organización Biológica",
            explanation = "El cuerpo humano está estructurado de lo más simple a lo más complejo:\n1. Célula: Unidad básica de la vida.\n2. Tejido: Grupo de células similares que trabajan juntas.\n3. Órgano: Estructura formada por tejidos (ej. corazón, estómago).\n4. Sistema: Conjunto de órganos coordinados (ej. sistema digestivo).\n5. Organismo: El ser vivo completo e integrado.",
            exampleText = "Ejemplo: Célula muscular → Tejido muscular → Corazón → Sistema circulatorio → Ser humano.",
            instructions = "Memoriza el orden secuencial desde la célula hasta el organismo completo.",
            visualResourceHint = "Esquema jerárquico multinivel con ilustraciones anatómicas simples.",
            origin = ContentOrigin.AUTORIZADO,
            status = ContentStatus.PUBLICADO,
            referenceTextbook = "Texto de Estudio Ciencias Naturales 6.º Básico, Mineduc, pág. 12-16"
        ),
        // HISTORIA
        ContentEntity(
            id = "cnt_hist_1",
            oaId = "oa_hist_15",
            title = "División de Poderes del Estado en Chile",
            explanation = "Para evitar el abuso de poder y garantizar la libertad de los ciudadanos, en la democracia chilena el poder se divide en tres ramas independientes:\n1. Poder Ejecutivo: Encabezado por el Presidente de la República (administra el país y ejecuta leyes).\n2. Poder Legislativo: Congresistas (Senadores y Diputados en el Congreso Nacional, crean y modifican leyes).\n3. Poder Judicial: Tribunales de Justicia y Corte Suprema (velan por el cumplimiento de la ley).",
            exampleText = "Ejemplo: El Congreso aprueba una ley de educación, el Presidente la promulga y los tribunales aseguran que todos la respeten.",
            instructions = "Identifica a las autoridades responsables de cada uno de los tres poderes del Estado.",
            visualResourceHint = "Infografía con los 3 edificios institucionales: La Moneda, Congreso Nacional y Palacio de Tribunales.",
            origin = ContentOrigin.PUBLICO,
            status = ContentStatus.PUBLICADO,
            referenceTextbook = "Texto de Estudio Historia 6.º Básico, Mineduc, pág. 110-116"
        ),
        // CONTENIDO ADICIONAL EN REVISIÓN PARA DEMO DE UTP
        ContentEntity(
            id = "cnt_leng_2",
            oaId = "oa_leng_03",
            title = "Borrador de Apoyo: El Ambiente Físico y Psicológico",
            explanation = "El ambiente físico describe el lugar geográfico, mientras que el ambiente psicológico se refiere al clima emocional (miedo, alegría, tensión) que rodea a los personajes.",
            exampleText = "Ejemplo: Un bosque oscuro y lluvioso genera una atmósfera de misterio.",
            instructions = "Revisar concordancia con OA 03 por UTP antes de autorizar.",
            visualResourceHint = "Ilustración de contraste entre día soleado y noche tormentosa.",
            origin = ContentOrigin.PROPIO,
            status = ContentStatus.EN_REVISION,
            referenceTextbook = "Texto de Estudio Lenguaje 6.º Básico, Mineduc, pág. 30"
        )
    )

    val questions = listOf(
        // PREGUNTAS PRÁCTICA - LENGUAJE (OA 03)
        ActivityQuestionEntity(
            id = "q_leng_p1",
            oaId = "oa_leng_03",
            contentId = "cnt_leng_1",
            title = "Actividad 1: Identificación del Conflicto",
            questionText = "¿Cuál es la función principal del conflicto en una historia narrada?",
            optionA = "Decorar el paisaje del relato.",
            optionB = "Impulsar las acciones y decisiones de los personajes.",
            optionC = "Explicar el significado de palabras difíciles.",
            optionD = "Indicar el número de páginas del libro.",
            correctAnswerIndex = 1,
            difficulty = "Fácil",
            isFinalTest = false
        ),
        ActivityQuestionEntity(
            id = "q_leng_p2",
            oaId = "oa_leng_03",
            contentId = "cnt_leng_1",
            title = "Desafío: Análisis del Entorno",
            questionText = "Si una historia transcurre en un desierto caluroso sin agua, ¿cómo influye el ambiente en el protagonista?",
            optionA = "Le obliga a buscar refugio y superar la sed para sobrevivir.",
            optionB = "No influye en nada porque los personajes no sienten el clima.",
            optionC = "Hace que el cuento se transforme automáticamente en una poesía.",
            optionD = "Aumenta la velocidad con la que lee el estudiante.",
            correctAnswerIndex = 0,
            difficulty = "Desafío",
            isFinalTest = false
        ),
        // PREGUNTAS PRUEBA FINAL - LENGUAJE (OA 03)
        ActivityQuestionEntity(
            id = "q_leng_f1",
            oaId = "oa_leng_03",
            contentId = "cnt_leng_1",
            title = "Prueba Final Lenguaje - Pregunta 1",
            questionText = "En un cuento, el personaje principal cambia de actitud cobarde a valiente tras rescatar a su mascota. Este cambio representa:",
            optionA = "La evolución del personaje motivada por el conflicto.",
            optionB = "Una falta de ortografía del autor.",
            optionC = "Un cambio injustificado de la historia.",
            optionD = "El resumen del índice del libro.",
            correctAnswerIndex = 0,
            difficulty = "Medio",
            isFinalTest = true
        ),
        ActivityQuestionEntity(
            id = "q_leng_f2",
            oaId = "oa_leng_03",
            contentId = "cnt_leng_1",
            title = "Prueba Final Lenguaje - Pregunta 2",
            questionText = "¿Qué elemento narrativo determina DÓNDE y CUÁNDO ocurren los hechos?",
            optionA = "El título secundario.",
            optionB = "El ambiente o entorno narrativo.",
            optionC = "El diálogo entre los lectores.",
            optionD = "El número de la unidad escolar.",
            correctAnswerIndex = 1,
            difficulty = "Fácil",
            isFinalTest = true
        ),

        // PREGUNTAS PRÁCTICA - MATEMÁTICA (OA 08)
        ActivityQuestionEntity(
            id = "q_mat_p1",
            oaId = "oa_mat_08",
            contentId = "cnt_mat_1",
            title = "Actividad 1: Conversión de Fracciones",
            questionText = "¿A qué número mixto equivale la fracción impropia 9/4?",
            optionA = "1 1/4",
            optionB = "2 1/4",
            optionC = "2 3/4",
            optionD = "3 1/2",
            correctAnswerIndex = 1,
            difficulty = "Medio",
            isFinalTest = false
        ),
        ActivityQuestionEntity(
            id = "q_mat_p2",
            oaId = "oa_mat_08",
            contentId = "cnt_mat_1",
            title = "Desafío: Representación Pictórica",
            questionText = "Si comiste 1 pizza entera y 2/3 de otra pizza, ¿qué fracción impropia representa el total comido?",
            optionA = "5/3",
            optionB = "3/5",
            optionC = "7/3",
            optionD = "2/3",
            correctAnswerIndex = 0,
            difficulty = "Desafío",
            isFinalTest = false
        ),
        // PREGUNTAS PRUEBA FINAL - MATEMÁTICA (OA 08)
        ActivityQuestionEntity(
            id = "q_mat_f1",
            oaId = "oa_mat_08",
            contentId = "cnt_mat_1",
            title = "Prueba Final Matemática - Pregunta 1",
            questionText = "¿Cuál de las siguientes opciones es una FRACCIÓN IMPROPIA?",
            optionA = "3/4",
            optionB = "1/2",
            optionC = "11/5",
            optionD = "5/8",
            correctAnswerIndex = 2,
            difficulty = "Fácil",
            isFinalTest = true
        ),
        ActivityQuestionEntity(
            id = "q_mat_f2",
            oaId = "oa_mat_08",
            contentId = "cnt_mat_1",
            title = "Prueba Final Matemática - Pregunta 2",
            questionText = "El número mixto 3 1/2 convertido a fracción impropia es equivalente a:",
            optionA = "7/2",
            optionB = "6/2",
            optionC = "5/2",
            optionD = "8/3",
            correctAnswerIndex = 0,
            difficulty = "Medio",
            isFinalTest = true
        ),

        // PREGUNTAS PRÁCTICA - CIENCIAS NATURALES (OA 02)
        ActivityQuestionEntity(
            id = "q_cnat_p1",
            oaId = "oa_cnat_02",
            contentId = "cnt_cnat_1",
            title = "Actividad 1: Niveles de Organización",
            questionText = "¿Cuál es el nivel MÁS SIMPLE de organización en los seres vivos?",
            optionA = "Tejido",
            optionB = "Sistema",
            optionC = "Célula",
            optionD = "Órgano",
            correctAnswerIndex = 2,
            difficulty = "Fácil",
            isFinalTest = false
        ),
        // PREGUNTAS PRUEBA FINAL - CIENCIAS NATURALES (OA 02)
        ActivityQuestionEntity(
            id = "q_cnat_f1",
            oaId = "oa_cnat_02",
            contentId = "cnt_cnat_1",
            title = "Prueba Final Ciencias - Pregunta 1",
            questionText = "El estómago, el corazón y los pulmones pertenecen al nivel de organización denominado:",
            optionA = "Tejido",
            optionB = "Órgano",
            optionC = "Célula",
            optionD = "Especie",
            correctAnswerIndex = 1,
            difficulty = "Fácil",
            isFinalTest = true
        ),
        ActivityQuestionEntity(
            id = "q_cnat_f2",
            oaId = "oa_cnat_02",
            contentId = "cnt_cnat_1",
            title = "Prueba Final Ciencias - Pregunta 2",
            questionText = "Un grupo de células especializadas que trabajan juntas para cumplir una misma función forman un:",
            optionA = "Tejido",
            optionB = "Sistema",
            optionC = "Organismo",
            optionD = "Planeta",
            correctAnswerIndex = 0,
            difficulty = "Medio",
            isFinalTest = true
        ),

        // PREGUNTAS PRÁCTICA - HISTORIA (OA 15)
        ActivityQuestionEntity(
            id = "q_hist_p1",
            oaId = "oa_hist_15",
            contentId = "cnt_hist_1",
            title = "Actividad 1: Poderes del Estado",
            questionText = "¿Qué autoridades forman parte del Poder Legislativo en Chile?",
            optionA = "El Presidente y los Ministros.",
            optionB = "Los Jueces de la Corte Suprema.",
            optionC = "Los Diputados y Senadores en el Congreso Nacional.",
            optionD = "Los Alcaldes de cada comuna.",
            correctAnswerIndex = 2,
            difficulty = "Medio",
            isFinalTest = false
        ),
        // PREGUNTAS PRUEBA FINAL - HISTORIA (OA 15)
        ActivityQuestionEntity(
            id = "q_hist_f1",
            oaId = "oa_hist_15",
            contentId = "cnt_hist_1",
            title = "Prueba Final Historia - Pregunta 1",
            questionText = "¿Por qué es importante la división de los tres poderes del Estado en una democracia?",
            optionA = "Para evitar que el poder se concentre en una sola persona o grupo.",
            optionB = "Para que el país tenga tres capitales diferentes.",
            optionC = "Para cobrar tres veces más impuestos.",
            optionD = "Para cambiar de presidente cada mes.",
            correctAnswerIndex = 0,
            difficulty = "Fácil",
            isFinalTest = true
        ),
        ActivityQuestionEntity(
            id = "q_hist_f2",
            oaId = "oa_hist_15",
            contentId = "cnt_hist_1",
            title = "Prueba Final Historia - Pregunta 2",
            questionText = "La norma fundamental que establece la organización del Estado y garantiza los derechos de las personas en Chile es:",
            optionA = "El reglamento escolar.",
            optionB = "La Constitución Política de la República.",
            optionC = "El libro de clases.",
            optionD = "Un contrato privado.",
            correctAnswerIndex = 1,
            difficulty = "Medio",
            isFinalTest = true
        )
    )

    val teacherConfigs = listOf(
        TeacherConfigEntity("oa_leng_03", maxAttemptsAllowed = 3, isAssignedToStudents = true),
        TeacherConfigEntity("oa_mat_08", maxAttemptsAllowed = 2, isAssignedToStudents = true),
        TeacherConfigEntity("oa_cnat_02", maxAttemptsAllowed = 3, isAssignedToStudents = true),
        TeacherConfigEntity("oa_hist_15", maxAttemptsAllowed = 3, isAssignedToStudents = true)
    )
}
