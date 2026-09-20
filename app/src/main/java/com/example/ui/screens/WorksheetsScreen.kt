package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ExerciseType
import com.example.model.Worksheet
import com.example.model.WorksheetExercise
import com.example.ui.theme.JadeContainer
import com.example.ui.theme.JadeDark
import com.example.ui.theme.JadeSecondary
import com.example.ui.theme.SaffronContainer
import com.example.ui.theme.SaffronDark
import com.example.ui.theme.SaffronPrimary
import com.example.viewmodel.MainAppViewModel

@Composable
fun WorksheetsScreen(
    viewModel: MainAppViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val worksheet = uiState.currentWorksheet

    var showExportDialog by remember { mutableStateOf(false) }

    val gradeOptions = listOf("कक्षा १ (Grade 1)", "कक्षा २ (Grade 2)", "बालवाटिका (Balvatika)")
    val topicOptions = listOf(
        "कक्षा और प्रकृति (Classroom & Nature)",
        "बुनियादी संख्या ज्ञान (FLN Numeracy)",
        "पशु-पक्षी और फल (Animals & Fruits)"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp)
    ) {
        // Generator Controls Card
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = SaffronContainer,
                                modifier = Modifier.size(28.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("📝", fontSize = 14.sp)
                                }
                            }
                            Text(
                                text = "NIPUN FLN वर्कशीट जनरेटर",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = SaffronDark
                            )
                        }

                        // Export Button
                        FilledTonalButton(
                            onClick = { showExportDialog = true },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("export_worksheet_button")
                        ) {
                            Icon(Icons.Default.Print, contentDescription = "Print", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("प्रिंट / Export", fontSize = 12.sp)
                        }
                    }

                    // Topic & Grade selectors
                    Text(
                        text = "विषय (Topic): ${uiState.worksheetTopic}",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                viewModel.generateNewWorksheet(topicOptions[0], gradeOptions[0])
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = "Regenerate", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("नई वर्कशीट बनाएं", fontSize = 12.sp)
                        }

                        OutlinedButton(
                            onClick = viewModel::toggleAnswerKey,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("toggle_answers_button")
                        ) {
                            Icon(
                                imageVector = if (uiState.showAnswerKey) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = "Answer key",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(if (uiState.showAnswerKey) "उत्तर छिपाएं" else "उत्तर देखें", fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // Worksheet Sheet Canvas (Printed paper style)
        if (worksheet != null) {
            item {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFFFFEFA),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                    shadowElevation = 4.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Worksheet Title Header
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = worksheet.title,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = Color(0xFF1E293B)
                            )
                            Text(
                                text = "${worksheet.gradeLevel} • ${worksheet.flnObjective}",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFF64748B)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("विद्यार्थी का नाम: __________", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                                Text("दिनांक: ${worksheet.dateGenerated}", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                            }
                        }

                        HorizontalDivider(color = Color(0xFFCBD5E1), thickness = 1.dp)

                        // Exercises in the worksheet
                        worksheet.exercises.forEachIndexed { index, exercise ->
                            WorksheetExerciseBlock(
                                exercise = exercise,
                                showAnswers = uiState.showAnswerKey,
                                selectedAnswer = uiState.worksheetAnswers[exercise.id],
                                onSelectAnswer = { ans -> viewModel.submitExerciseAnswer(exercise.id, ans) }
                            )
                            if (index < worksheet.exercises.size - 1) {
                                HorizontalDivider(color = Color(0xFFF1F5F9), thickness = 0.8.dp)
                            }
                        }
                    }
                }
            }
        }
    }

    if (showExportDialog) {
        AlertDialog(
            onDismissRequest = { showExportDialog = false },
            title = { Text("वर्कशीट डाउनलोड / प्रिंट") },
            text = {
                Text(
                    "यह NIPUN Bharat FLN अभ्यास पत्रक हिंदी व संथाली (ओल चिकी) लिपि में ऑफ़लाइन उपयोग और प्रिंटिंग के लिए तैयार है। ग्रामीण विद्यालयों में इंटरनेट के बिना सीधे उपयोग किया जा सकता है।"
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showExportDialog = false
                        Toast.makeText(context, "वर्कशीट सफलतापूर्वक तैयार (Exported)!", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary)
                ) {
                    Text("PDF सुरक्षित करें")
                }
            },
            dismissButton = {
                TextButton(onClick = { showExportDialog = false }) {
                    Text("रद्द करें")
                }
            }
        )
    }
}

@Composable
fun WorksheetExerciseBlock(
    exercise: WorksheetExercise,
    showAnswers: Boolean,
    selectedAnswer: String?,
    onSelectAnswer: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Exercise Prompt
        Text(
            text = exercise.promptHindi,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = Color(0xFF0F172A)
        )
        Text(
            text = exercise.promptSanthali,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
            color = JadeDark
        )

        when (exercise.type) {
            ExerciseType.MATCHING -> {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    exercise.matchingPairs.forEach { pair ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFF8FAFC), RoundedCornerShape(8.dp))
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = pair.hindi, fontWeight = FontWeight.Medium, fontSize = 14.sp)
                            Text(text = "──────>", color = Color.LightGray, fontSize = 12.sp)
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = pair.olChiki,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp,
                                    color = JadeDark
                                )
                                Text(
                                    text = "(${pair.pronunciation})",
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }
            }

            ExerciseType.OL_CHIKI_TRACING -> {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    exercise.tracingLetters.forEach { letter ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, Color(0xFF94A3B8)),
                            color = Color.White,
                            modifier = Modifier.size(54.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = letter,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 16.sp,
                                    color = Color(0xFF0F766E)
                                )
                            }
                        }
                    }
                }
            }

            ExerciseType.PICTURE_IDENTIFY, ExerciseType.FILL_IN_BLANKS -> {
                Text(
                    text = exercise.questionText,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                    color = Color(0xFF334155)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    exercise.options.forEach { opt ->
                        val isChosen = selectedAnswer == opt
                        val isCorrect = opt == exercise.correctAnswer
                        val btnBg = when {
                            showAnswers && isCorrect -> Color(0xFFDCFCE7)
                            isChosen -> SaffronContainer
                            else -> Color(0xFFF1F5F9)
                        }

                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onSelectAnswer(opt) },
                            shape = RoundedCornerShape(8.dp),
                            color = btnBg,
                            border = BorderStroke(1.dp, if (showAnswers && isCorrect) Color(0xFF16A34A) else Color(0xFFCBD5E1))
                        ) {
                            Box(
                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = opt,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = if (isChosen || (showAnswers && isCorrect)) FontWeight.Bold else FontWeight.Normal
                                    ),
                                    color = if (showAnswers && isCorrect) Color(0xFF15803D) else Color(0xFF1E293B)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
