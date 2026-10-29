package com.example.proyectoappsmobiles.ui.docente

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.proyectoappsmobiles.data.local.entity.OaEntity
import com.example.proyectoappsmobiles.data.local.entity.PedagogicalReportEntity
import com.example.proyectoappsmobiles.data.local.entity.TeacherConfigEntity
import com.example.proyectoappsmobiles.data.model.ReportStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocenteScreen(
    viewModel: DocenteViewModel,
    onBack: () -> Unit
) {
    val oas by viewModel.oas.collectAsState()
    val teacherConfigs by viewModel.teacherConfigs.collectAsState()
    val reports by viewModel.reports.collectAsState()

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Column {
                            Text(text = "Módulo Docente", fontWeight = FontWeight.Bold)
                            Text(text = "Prof. Roberto Gómez • 6.º Básico A", style = MaterialTheme.typography.bodyMedium)
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )

                TabRow(selectedTabIndex = selectedTabIndex) {
                    Tab(
                        selected = selectedTabIndex == 0,
                        onClick = { selectedTabIndex = 0 },
                        text = { Text("Asignación & Intentos") },
                        icon = {
                            Icon(
                                Icons.Default.Settings,
                                contentDescription = null,
                                tint = if (selectedTabIndex == 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    )
                    Tab(
                        selected = selectedTabIndex == 1,
                        onClick = { selectedTabIndex = 1 },
                        text = { Text("Informes Pedagógicos (${reports.size})") },
                        icon = {
                            Icon(
                                Icons.Default.AssignmentTurnedIn,
                                contentDescription = null,
                                tint = if (selectedTabIndex == 1) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            when (selectedTabIndex) {
                0 -> OaAssignmentTab(
                    oas = oas,
                    teacherConfigs = teacherConfigs,
                    onUpdateAttempts = { oaId, attempts -> viewModel.updateMaxAttempts(oaId, attempts) },
                    onToggleAssign = { oaId, assigned -> viewModel.toggleAssignment(oaId, assigned) }
                )

                1 -> ReportsTab(
                    reports = reports,
                    onValidateReport = { reportId ->
                        viewModel.validatePedagogicalReport(reportId, "Prof. Roberto Gómez")
                        Toast.makeText(context, "Informe validado manualmente por el docente", Toast.LENGTH_SHORT).show()
                    },
                    onDownloadReport = { report ->
                        Toast.makeText(context, "Informe descargado en formato texto/PDF local", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }
}

@Composable
private fun OaAssignmentTab(
    oas: List<OaEntity>,
    teacherConfigs: List<TeacherConfigEntity>,
    onUpdateAttempts: (String, Int) -> Unit,
    onToggleAssign: (String, Boolean) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Configuración del Curso (6.º Básico A)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Selecciona qué OA están activos para los estudiantes y establece el límite de intentos permitidos para la prueba final.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }

        items(oas) { oa ->
            val config = teacherConfigs.find { it.oaId == oa.id }
                ?: TeacherConfigEntity(oaId = oa.id, maxAttemptsAllowed = 3, isAssignedToStudents = true)

            OaConfigCard(
                oa = oa,
                config = config,
                onUpdateAttempts = { newAttempts -> onUpdateAttempts(oa.id, newAttempts) },
                onToggleAssign = { assigned -> onToggleAssign(oa.id, assigned) }
            )
        }
    }
}

@Composable
private fun OaConfigCard(
    oa: OaEntity,
    config: TeacherConfigEntity,
    onUpdateAttempts: (Int) -> Unit,
    onToggleAssign: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = oa.code,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer // Corrección aplicada aquí
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (config.isAssignedToStudents) "Asignado" else "Oculto",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Switch(
                        checked = config.isAssignedToStudents,
                        onCheckedChange = onToggleAssign
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = oa.description,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Límite de Intentos para la Prueba Final:",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                (1..5).forEach { attemptCount ->
                    FilterChip(
                        selected = config.maxAttemptsAllowed == attemptCount,
                        onClick = { onUpdateAttempts(attemptCount) },
                        label = { Text("$attemptCount ${if (attemptCount == 1) "intento" else "intentos"}") }
                    )
                }
            }
        }
    }
}

@Composable
private fun ReportsTab(
    reports: List<PedagogicalReportEntity>,
    onValidateReport: (String) -> Unit,
    onDownloadReport: (PedagogicalReportEntity) -> Unit
) {
    if (reports.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Aún no se han generado informes pedagógicos. Aparecerán aquí cuando un estudiante rinda una prueba final.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(reports) { report ->
                ReportCard(
                    report = report,
                    onValidate = { onValidateReport(report.id) },
                    onDownload = { onDownloadReport(report) }
                )
            }
        }
    }
}

@Composable
private fun ReportCard(
    report: PedagogicalReportEntity,
    onValidate: () -> Unit,
    onDownload: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Estudiante: ${report.studentFictionalName}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${report.subjectTitle} • ${report.oaCode}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant // Corrección aplicada aquí
                    )
                }

                Surface(
                    color = if (report.status == ReportStatus.VALIDADO) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.errorContainer,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = report.status.displayName,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (report.status == ReportStatus.VALIDADO) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Rendimiento: ${report.scorePercentage}%",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Correctas: ${report.correctCount} / ${report.totalCount}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Fortalezas:",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = report.strengthsSummary,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Contenidos a Reforzar:",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = report.reinforcementSummary,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Fecha: ${report.dateFormatted} • Docente: ${report.teacherResponsible}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onDownload,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Descargar", color = MaterialTheme.colorScheme.primary)
                }

                if (report.status != ReportStatus.VALIDADO) {
                    Button(
                        onClick = onValidate,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Validar", color = Color.White)
                    }
                }
            }
        }
    }
}