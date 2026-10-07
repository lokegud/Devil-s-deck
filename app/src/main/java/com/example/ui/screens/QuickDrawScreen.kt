package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DeckCategory
import com.example.ui.components.DevilPromptCardView
import com.example.ui.components.GrindBackground
import com.example.ui.theme.BebasNeueFontFamily
import com.example.ui.theme.DevilBlood
import com.example.ui.theme.DevilCrimson
import com.example.ui.theme.DevilGold
import com.example.ui.theme.MontserratFontFamily
import com.example.ui.theme.SinBorder
import com.example.ui.theme.SinGrey
import com.example.ui.theme.SinSurfaceVariant
import com.example.ui.theme.SinWhite
import com.example.viewmodel.AppScreen
import com.example.viewmodel.GameViewModel

@Composable
fun QuickDrawScreen(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    BackHandler {
        viewModel.navigateTo(AppScreen.HOME)
    }

    GrindBackground(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { viewModel.navigateTo(AppScreen.HOME) },
                    modifier = Modifier.testTag("quick_draw_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = SinWhite
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                    Text(
                        text = "QUICK DRAW PROMPT FLIPPER",
                        color = SinWhite,
                        fontSize = 20.sp,
                        fontFamily = BebasNeueFontFamily,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Instant cards for casual party games with no setup",
                        color = SinGrey,
                        fontSize = 11.sp,
                        fontFamily = MontserratFontFamily
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Category Filter Chips
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    val isAllSelected = uiState.quickDrawCategoryFilter == null
                    FilterChip(
                        selected = isAllSelected,
                        onClick = { viewModel.shuffleQuickDraw(null) },
                        label = {
                            Text(
                                text = "🔥 ALL DECKS",
                                fontFamily = MontserratFontFamily,
                                fontSize = 12.sp,
                                fontWeight = if (isAllSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = DevilBlood,
                            selectedLabelColor = SinWhite,
                            containerColor = SinSurfaceVariant,
                            labelColor = SinGrey
                        )
                    )
                }

                items(DeckCategory.entries.filterNot { it == DeckCategory.CUSTOM }) { cat ->
                    val isSelected = uiState.quickDrawCategoryFilter == cat
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.shuffleQuickDraw(cat) },
                        label = {
                            Text(
                                text = "${cat.emoji} ${cat.title}",
                                fontFamily = MontserratFontFamily,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = DevilBlood,
                            selectedLabelColor = SinWhite,
                            containerColor = SinSurfaceVariant,
                            labelColor = SinGrey
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // The Card Display
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                uiState.quickDrawPrompt?.let { prompt ->
                    DevilPromptCardView(
                        prompt = prompt,
                        modifier = Modifier.fillMaxSize(),
                        onCardClick = { viewModel.shuffleQuickDraw(uiState.quickDrawCategoryFilter) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Quick Draw Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = { viewModel.drawPunishmentForLoser() },
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .testTag("quick_punishment_button"),
                    border = BorderStroke(1.2.dp, DevilBlood),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "DRAW DARE 💀",
                        color = DevilCrimson,
                        fontFamily = BebasNeueFontFamily,
                        fontSize = 15.sp,
                        letterSpacing = 0.5.sp
                    )
                }

                Button(
                    onClick = { viewModel.shuffleQuickDraw(uiState.quickDrawCategoryFilter) },
                    modifier = Modifier
                        .weight(1.3f)
                        .height(52.dp)
                        .testTag("next_quick_prompt_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = DevilCrimson),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.NavigateNext,
                        contentDescription = "Next Card",
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "NEXT PROMPT ➔",
                        fontFamily = BebasNeueFontFamily,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Show Punishment Dialog if triggered
        if (uiState.showPunishmentDialog && uiState.currentPunishment != null) {
            com.example.ui.components.DevilPunishmentDialog(
                punishment = uiState.currentPunishment!!,
                onDismiss = { viewModel.dismissPunishmentDialog() },
                onReroll = { viewModel.rerollPunishment() },
                onAccept = { viewModel.dismissPunishmentDialog() }
            )
        }
    }
}
