package com.example.ui.screens

import android.content.Context
import androidx.compose.animation.core.*
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Flip
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.FlashcardCategory
import com.example.model.FlashcardItem
import com.example.ui.theme.JadeContainer
import com.example.ui.theme.JadeDark
import com.example.ui.theme.JadeSecondary
import com.example.ui.theme.SaffronContainer
import com.example.ui.theme.SaffronDark
import com.example.ui.theme.SaffronPrimary
import com.example.viewmodel.MainAppViewModel

@Composable
fun FlashcardsScreen(
    viewModel: MainAppViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val filteredCards = viewModel.getFilteredFlashcards()
    val currentIndex = uiState.activeFlashcardIndex.coerceIn(0, (filteredCards.size - 1).coerceAtLeast(0))
    val currentCard = filteredCards.getOrNull(currentIndex)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp)
    ) {
        // Header Banner
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SaffronContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = SaffronDark,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text("🗂️", fontSize = 16.sp)
                        }
                    }
                    Column {
                        Text(
                            text = "NIPUN Bharat सचित्र फ्लैशकार्ड",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = SaffronDark
                        )
                        Text(
                            text = "बुनियादी साक्षरता (FLN) - हिंदी व संथाली ओल चिकी शब्द संवर्धन",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Category Filter Chips
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(FlashcardCategory.values()) { cat ->
                    val isSelected = uiState.flashcardFilter == cat
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.setFlashcardFilter(cat) },
                        label = { Text("${cat.labelHindi} (${cat.labelEnglish})", fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                }
            }
        }

        // Active Interactive Flashcard with Flip Effect
        if (currentCard != null) {
            item {
                InteractiveFlashcard(
                    card = currentCard,
                    isFlipped = uiState.isCardFlipped,
                    onFlip = viewModel::flipCard,
                    onSpeak = { viewModel.speakSanthali(context, currentCard.santhaliOlChiki) },
                    cardIndex = currentIndex + 1,
                    totalCards = filteredCards.size
                )
            }

            // Prev / Next Navigation Buttons
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = viewModel::prevCard,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("prev_flashcard_button")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous")
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("पिछला कार्ड")
                    }

                    FilledTonalButton(
                        onClick = viewModel::flipCard,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        ),
                        modifier = Modifier.testTag("flip_flashcard_button")
                    ) {
                        Icon(Icons.Default.Flip, contentDescription = "Flip Card")
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(if (uiState.isCardFlipped) "हिंदी देखें" else "ओल चिकी देखें")
                    }

                    Button(
                        onClick = viewModel::nextCard,
                        colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("next_flashcard_button")
                    ) {
                        Text("अगला कार्ड")
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next")
                    }
                }
            }
        }

        // Quick Deck List below
        item {
            Text(
                text = "सभी फ्लैशकार्ड सूची (${filteredCards.size})",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        items(filteredCards) { card ->
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        val idx = filteredCards.indexOf(card)
                        if (idx >= 0) {
                            viewModel.nextCard() // Trigger state update
                        }
                    },
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(card.emojiIcon, fontSize = 24.sp)
                        Column {
                            Text(
                                text = "${card.hindiWord} ➔ ${card.santhaliOlChiki}",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "${card.pronunciationLatin} (${card.englishMeaning})",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    IconButton(
                        onClick = { viewModel.speakSanthali(context, card.santhaliOlChiki) },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                            contentDescription = "Speak",
                            tint = JadeSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InteractiveFlashcard(
    card: FlashcardItem,
    isFlipped: Boolean,
    onFlip: () -> Unit,
    onSpeak: () -> Unit,
    cardIndex: Int,
    totalCards: Int
) {
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing),
        label = "card_flip"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
            .clickable { onFlip() }
            .testTag("interactive_flashcard_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isFlipped) Color(0xFFF0FDF4) else Color(0xFFFFFBEB)
        ),
        border = BorderStroke(
            2.dp,
            if (isFlipped) Color(0xFF86EFAC) else Color(0xFFFDE68A)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            // Top Bar: Card Counter & Audio
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
                ) {
                    Text(
                        text = "कार्ड $cardIndex / $totalCards",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                IconButton(
                    onClick = onSpeak,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(JadeContainer)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                        contentDescription = "Pronounce",
                        tint = JadeDark,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Center Content: Front (Hindi) vs Back (Ol Chiki Santhali)
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = card.emojiIcon,
                    fontSize = 52.sp
                )

                if (!isFlipped) {
                    // FRONT: Hindi Word
                    Text(
                        text = card.hindiWord,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 32.sp
                        ),
                        color = SaffronDark
                    )
                    Text(
                        text = "${card.englishMeaning} (English)",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF78350F)
                    )
                    Text(
                        text = "👆 छूकर संथाली (ओल चिकी) देखें",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                } else {
                    // BACK: Santhali in Unicode Ol Chiki
                    Text(
                        text = card.santhaliOlChiki,
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.4.sp,
                            fontSize = 34.sp
                        ),
                        color = Color(0xFF064E3B)
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Surface(shape = RoundedCornerShape(6.dp), color = Color(0xFFDCFCE7)) {
                            Text(
                                text = "उ: ${card.pronunciationLatin}",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                color = Color(0xFF166534),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Surface(shape = RoundedCornerShape(6.dp), color = Color(0xFFFEF3C7)) {
                            Text(
                                text = card.pronunciationDevanagari,
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                                color = Color(0xFF92400E),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "उदा: ${card.sentenceHindi}",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF334155)
                    )
                    Text(
                        text = card.sentenceOlChiki,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                        color = Color(0xFF0F766E)
                    )
                }
            }

            // Flip hint at bottom
            Text(
                text = "टैप करके पलटें 🔄",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}
