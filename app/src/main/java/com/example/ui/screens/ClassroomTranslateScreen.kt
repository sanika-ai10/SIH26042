package com.example.ui.screens

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ClassroomPhrase
import com.example.model.PhraseCategory
import com.example.model.TranslationResult
import com.example.ui.components.AudioWaveformVisualizer
import com.example.ui.components.LatencyPill
import com.example.ui.theme.JadeContainer
import com.example.ui.theme.JadeDark
import com.example.ui.theme.JadeSecondary
import com.example.ui.theme.OnJadeContainer
import com.example.ui.theme.OnSaffronContainer
import com.example.ui.theme.SaffronContainer
import com.example.ui.theme.SaffronDark
import com.example.ui.theme.SaffronPrimary
import com.example.viewmodel.MainAppViewModel

@Composable
fun ClassroomTranslateScreen(
    viewModel: MainAppViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val isListening by viewModel.isListening.collectAsState()
    val isSpeaking by viewModel.isSpeaking.collectAsState()
    val context = LocalContext.current

    val allPhrases = remember { viewModel.translationService.getCuratedClassroomPhrases() }
    val filteredPhrases = remember(uiState.selectedCategory, uiState.phraseSearchQuery) {
        val byCat = if (uiState.selectedCategory == PhraseCategory.ALL) {
            allPhrases
        } else {
            allPhrases.filter { it.category == uiState.selectedCategory }
        }
        if (uiState.phraseSearchQuery.isBlank()) {
            byCat
        } else {
            byCat.filter {
                it.hindi.contains(uiState.phraseSearchQuery, ignoreCase = true) ||
                it.pronunciationLatin.contains(uiState.phraseSearchQuery, ignoreCase = true) ||
                it.englishMeaning.contains(uiState.phraseSearchQuery, ignoreCase = true)
            }
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp)
    ) {
        // Teacher Speech / Input Card (Hindi)
        item {
            TeacherInputCard(
                inputText = uiState.hindiInput,
                onTextChanged = viewModel::onHindiInputChanged,
                onTranslate = viewModel::translateInput,
                isListening = isListening,
                onStartListening = { viewModel.startListening(context) },
                onStopListening = viewModel::stopListening,
                context = context
            )
        }

        // Student Output Card (Santhali / Ol Chiki) - Matching slide 2 mockup
        item {
            StudentOlChikiCard(
                translation = uiState.activeTranslation,
                isTranslating = uiState.isTranslating,
                isSpeaking = isSpeaking,
                speechRate = uiState.speechRate,
                onSpeak = { viewModel.speakSanthali(context) },
                onStopSpeaking = viewModel::stopSpeaking,
                onRateChanged = viewModel::setSpeechRate
            )
        }

        // Quick Category Filter Bar
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "कक्षा के त्वरित वाक्य (Quick Phrases)",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${filteredPhrases.size} वाक्यांश",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                // Search field for phrases
                OutlinedTextField(
                    value = uiState.phraseSearchQuery,
                    onValueChange = viewModel::onPhraseSearch,
                    placeholder = { Text("वाक्यांश खोजें (Search phrases)...", fontSize = 13.sp) },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Search", modifier = Modifier.size(18.dp))
                    },
                    trailingIcon = {
                        if (uiState.phraseSearchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.onPhraseSearch("") }) {
                                Icon(Icons.Default.Close, contentDescription = "Clear", modifier = Modifier.size(16.dp))
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("phrase_search_field")
                )

                // Category chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 4.dp)
                ) {
                    items(PhraseCategory.values()) { cat ->
                        val isSelected = uiState.selectedCategory == cat
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.setPhraseCategory(cat) },
                            label = { Text("${cat.icon} ${cat.displayName}", fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                    }
                }
            }
        }

        // Phrase Cards
        items(filteredPhrases) { phrase ->
            ClassroomPhraseRow(
                phrase = phrase,
                onSelect = { viewModel.selectPresetPhrase(phrase, context) },
                onPlayAudio = { viewModel.speakSanthali(context, phrase.santhaliOlChiki) },
                isBookmarked = uiState.bookmarkedPhraseIds.contains(phrase.id),
                onToggleBookmark = { viewModel.toggleBookmark(phrase.id) }
            )
        }
    }
}

@Composable
fun TeacherInputCard(
    inputText: String,
    onTextChanged: (String) -> Unit,
    onTranslate: () -> Unit,
    isListening: Boolean,
    onStartListening: () -> Unit,
    onStopListening: () -> Unit,
    context: Context
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("teacher_input_card"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Card Header
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
                            Text("👨‍🏫", fontSize = 14.sp)
                        }
                    }
                    Text(
                        text = "शिक्षक (Teacher - Hindi Speech/Text)",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                        color = SaffronDark
                    )
                }

                if (isListening) {
                    AudioWaveformVisualizer(isListening = true)
                }
            }

            // Input Text Field
            OutlinedTextField(
                value = inputText,
                onValueChange = onTextChanged,
                placeholder = {
                    Text(
                        text = "यहाँ हिंदी में बोलें या लिखें (उदा: किताब खोलो)...",
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("hindi_input_field"),
                shape = RoundedCornerShape(14.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { onTranslate() }),
                minLines = 2,
                maxLines = 4
            )

            // Action Buttons: Mic (Part 1 ASR) and Translate button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Mic Button
                FilledTonalButton(
                    onClick = {
                        if (isListening) onStopListening() else onStartListening()
                    },
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = if (isListening) Color(0xFFFFE4E6) else SaffronContainer,
                        contentColor = if (isListening) Color(0xFFE11D48) else SaffronDark
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.testTag("mic_speech_button")
                ) {
                    Icon(
                        imageVector = if (isListening) Icons.Default.MicOff else Icons.Default.Mic,
                        contentDescription = "Microphone",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isListening) "सुन रहा है..." else "बोलें (Mic)",
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Translate / Send Button
                Button(
                    onClick = onTranslate,
                    colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.testTag("translate_action_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Translate",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("अनुवाद करें", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun StudentOlChikiCard(
    translation: TranslationResult,
    isTranslating: Boolean,
    isSpeaking: Boolean,
    speechRate: Float,
    onSpeak: () -> Unit,
    onStopSpeaking: () -> Unit,
    onRateChanged: (Float) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("student_output_card"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
        border = BorderStroke(1.5.dp, Color(0xFFA7F3D0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header with Student Tag & Latency Pill
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
                        color = JadeContainer,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text("🎒", fontSize = 14.sp)
                        }
                    }
                    Text(
                        text = "छात्र (Student - Santhali / Ol Chiki)",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = JadeDark
                    )
                }

                LatencyPill(
                    latencyMs = translation.latencyMs,
                    isLocalCache = translation.isFromLocalCache
                )
            }

            if (isTranslating) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(4.dp)),
                    color = JadeSecondary
                )
            }

            // PRIMARY OL CHIKI DISPLAY (Prominent unicode typography for young students)
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = Color.White,
                border = BorderStroke(1.dp, Color(0xFFD1FAE5))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = translation.santhaliOlChiki,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.2.sp,
                            fontSize = 28.sp
                        ),
                        color = Color(0xFF064E3B)
                    )

                    HorizontalDivider(color = Color(0xFFE2E8F0), thickness = 0.8.dp)

                    // Pronunciation and Meaning Guides
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "उच्चारण (Pronunciation):",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = Color(0xFFE0F2FE)
                                ) {
                                    Text(
                                        text = translation.pronunciationLatin,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF0369A1)
                                        ),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = Color(0xFFFEF3C7)
                                ) {
                                    Text(
                                        text = translation.pronunciationDevanagari,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontWeight = FontWeight.Medium,
                                            color = Color(0xFFB45309)
                                        ),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        Text(
                            text = translation.englishMeaning,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                            color = Color(0xFF475569)
                        )
                    }
                }
            }

            // Audio Playback Controls (Part 3 Santhali TTS)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Audio Speak Button
                Button(
                    onClick = {
                        if (isSpeaking) onStopSpeaking() else onSpeak()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSpeaking) Color(0xFFDC2626) else JadeSecondary
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.testTag("santhali_tts_button")
                ) {
                    Icon(
                        imageVector = if (isSpeaking) Icons.Default.Stop else Icons.AutoMirrored.Filled.VolumeUp,
                        contentDescription = "Speak Santhali",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isSpeaking) "रुकें (Stop)" else "संथाली में सुनाएं (TTS)",
                        fontWeight = FontWeight.Bold
                    )
                }

                // Speech Rate Toggle
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "गति: ${if (speechRate < 0.9f) "धीमी (Slow)" else "सामान्य"}",
                        style = MaterialTheme.typography.labelSmall,
                        color = JadeDark
                    )
                    IconButton(
                        onClick = {
                            onRateChanged(if (speechRate < 0.9f) 1.0f else 0.75f)
                        },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Speed,
                            contentDescription = "Speech rate",
                            tint = JadeDark,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ClassroomPhraseRow(
    phrase: ClassroomPhrase,
    onSelect: () -> Unit,
    onPlayAudio: () -> Unit,
    isBookmarked: Boolean,
    onToggleBookmark: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() }
            .testTag("phrase_row_${phrase.id}"),
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
        tonalElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = phrase.hindi,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = phrase.santhaliOlChiki,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    ),
                    color = JadeDark
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${phrase.pronunciationLatin} • ${phrase.englishMeaning}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onPlayAudio,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                        contentDescription = "Play phrase audio",
                        tint = JadeSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                IconButton(
                    onClick = onToggleBookmark,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Bookmark",
                        tint = if (isBookmarked) SaffronPrimary else MaterialTheme.colorScheme.outline,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
