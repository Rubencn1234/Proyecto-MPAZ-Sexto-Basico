package com.example.proyectoappsmobiles.ui.estudiante

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.proyectoappsmobiles.data.local.entity.ActivityQuestionEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EstudianteTestScreen(
    viewModel: EstudianteViewModel,
    onBack: () -> Unit
) {
    val selectedSubject by viewModel.selectedSubject.collectAsState()
    val finalQuestions by viewModel.finalQuestions.collectAsState()
    val teacherConfig by viewModel.teacherConfig.collectAsState()
    val progress by viewModel.progress.collectAsState()

    val context = LocalContext.current
    val userAnswers = remember { mutableStateMapOf<String, Int>() }
    var isEvaluated by remember { mutableStateOf(false) }

    val attemptsUsed = progress?.attemptsUsed ?: 0
    val maxAttempts = teacherConfig?.maxAttemptsAllowed ?: 3
    val isLocked = attemptsUsed >= maxAttempts && !isEvaluated

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(text = "Prueba Final del OA", fontWeight = FontWeight.Bold)
                        Text(text = "${selectedSubject?.title ?: ""} • Intento ${minOf(attemptsUsed + 1, maxAttempts)} de $maxAttempts", style = MaterialTheme.typography.bodyMedium)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            if (isLocked) {
                // Locked Test Screen
                TestLockedCard(
                    attemptsUsed = attemptsUsed,
                    maxAttempts = maxAttempts,
                    bestScore = progress?.bestScorePercentage ?: 0,
                    onBack = onBack
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(imageVector = Icons.Default.Quiz, contentDescription = null)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Evaluación Sumativa Final",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Responde todas las preguntas antes de enviar la prueba. Al finalizar verás tus respuestas correctas e incorrectas y se generará un informe para tu docente.",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }

                    items(finalQuestions) { q ->
                        TestQuestionCard(
                            question = q,
                            selectedOption = userAnswers[q.id],
                            isEvaluated = isEvaluated,
                            onOptionSelect = { selectedIdx ->
                                if (!isEvaluated) userAnswers[q.id] = selectedIdx
                            }
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(12.dp))

                        if (!isEvaluated) {
                            Button(
                                onClick = {
                                    val oaId = finalQuestions.firstOrNull()?.oaId ?: ""
                                    viewModel.submitFinalTest(
                                        oaId = oaId,
                                        selectedAnswers = userAnswers,
                                        questions = finalQuestions,
                                        onComplete = { updatedProgress ->
                                            isEvaluated = true
                                            Toast.makeText(context, "Prueba enviada. Rendimiento: ${updatedProgress.bestScorePercentage}%", Toast.LENGTH_LONG).show()
                                        }
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                enabled = userAnswers.size == finalQuestions.size && finalQuestions.isNotEmpty(),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text(
                                    text = "FINALIZAR Y ENVIAR PRUEBA",
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        } else {
                            Surface(
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "¡Evaluación Completada!",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Se ha guardado tu resultado y se ha enviado el Informe Pedagógico al docente para su revisión y validación manual.",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Button(
                                        onClick = onBack,
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                                    ) {
                                        Text("VOLVER AL MENÚ DE ASIGNATURAS")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TestLockedCard(
    attemptsUsed: Int,
    maxAttempts: Int,
    bestScore: Int,
    onBack: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Prueba Bloqueada por Intentos Agotados",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Has utilizado $attemptsUsed de $maxAttempts intentos permitidos configurados por tu docente para esta evaluación.",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Tu mejor rendimiento registrado fue: $bestScore%",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("VOLVER")
            }
        }
    }
}

@Composable
private fun TestQuestionCard(
    question: ActivityQuestionEntity,
    selectedOption: Int?,
    isEvaluated: Boolean,
    onOptionSelect: (Int) -> Unit
) {
    val options = listOf(question.optionA, question.optionB, question.optionC, question.optionD)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = question.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = question.questionText,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(12.dp))

            options.forEachIndexed { index, optionText ->
                val isSelected = selectedOption == index
                val isCorrectOption = index == question.correctAnswerIndex

                val cardBg = if (isEvaluated) {
                    when {
                        isCorrectOption -> MaterialTheme.colorScheme.secondaryContainer
                        isSelected -> MaterialTheme.colorScheme.errorContainer
                        else -> MaterialTheme.colorScheme.surfaceVariant
                    }
                } else if (isSelected) {
                    MaterialTheme.colorScheme.primaryContainer
                } else {
                    MaterialTheme.colorScheme.surfaceVariant
                }

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable(enabled = !isEvaluated) { onOptionSelect(index) },
                    shape = RoundedCornerShape(12.dp),
                    color = cardBg
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = isSelected,
                            onClick = { if (!isEvaluated) onOptionSelect(index) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = optionText,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.weight(1f)
                        )

                        if (isEvaluated && isCorrectOption) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.secondary
                            )
                        } else if (isEvaluated && isSelected && !isCorrectOption) {
                            Icon(
                                imageVector = Icons.Default.Error,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }
}
