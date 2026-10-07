package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.Player
import com.example.data.model.RoundPhase
import com.example.ui.components.DevilHeader
import com.example.ui.components.DevilPromptCardView
import com.example.ui.components.DevilPunishmentDialog
import com.example.ui.components.GrindBackground
import com.example.ui.components.PlayerScoreBar
import com.example.ui.components.RoundTimerView
import com.example.ui.theme.BebasNeueFontFamily
import com.example.ui.theme.DevilBlood
import com.example.ui.theme.DevilCrimson
import com.example.ui.theme.DevilGold
import com.example.ui.theme.DevilRed
import com.example.ui.theme.MontserratFontFamily
import com.example.ui.theme.SinBlack
import com.example.ui.theme.SinBorder
import com.example.ui.theme.SinGrey
import com.example.ui.theme.SinSurface
import com.example.ui.theme.SinSurfaceVariant
import com.example.ui.theme.SinWhite
import com.example.viewmodel.AppScreen
import com.example.viewmodel.GameViewModel

@Composable
fun ActiveGameScreen(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    var showQuitDialog by remember { mutableStateOf(false) }
    var showHistoryDialog by remember { mutableStateOf(false) }

    val currentDevil = uiState.players.getOrNull(uiState.currentDevilIndex)

    BackHandler {
        showQuitDialog = true
    }

    GrindBackground(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // Top Bar: Quit & History
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { showQuitDialog = true },
                    modifier = Modifier.testTag("game_quit_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Leave Game",
                        tint = SinGrey
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "DEVIL'S DECK",
                        color = DevilCrimson,
                        fontSize = 18.sp,
                        fontFamily = BebasNeueFontFamily,
                        letterSpacing = 1.2.sp
                    )
                }

                IconButton(
                    onClick = { showHistoryDialog = true },
                    modifier = Modifier.testTag("game_history_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = "Round History",
                        tint = DevilGold
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Devil Header
            DevilHeader(
                currentDevil = currentDevil,
                roundNumber = uiState.roundNumber,
                targetScore = uiState.targetSins
            )

            // Timer (if active)
            if (uiState.timerTotalSeconds > 0) {
                Spacer(modifier = Modifier.height(8.dp))
                RoundTimerView(
                    totalSeconds = uiState.timerTotalSeconds,
                    remainingSeconds = uiState.timerRemainingSeconds,
                    isRunning = uiState.isTimerRunning,
                    onTogglePlayPause = { viewModel.toggleTimer() },
                    onResetTimer = { viewModel.resetTimer() }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Main Content Area: Prompt Card + Phase Controls
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                if (uiState.isGameOver && uiState.overallWinner != null) {
                    // Game Over Screen
                    GameOverCard(
                        winner = uiState.overallWinner!!,
                        onPlayAgain = { viewModel.startGame() },
                        onHome = { viewModel.navigateTo(AppScreen.HOME) }
                    )
                } else {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        // The Black Prompt Card
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1.1f)
                        ) {
                            uiState.currentPrompt?.let { prompt ->
                                DevilPromptCardView(
                                    prompt = prompt,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Phase Action Controls
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(0.9f)
                        ) {
                            when (uiState.roundPhase) {
                                RoundPhase.PROMPT_REVEAL -> {
                                    PromptRevealControls(
                                        devilName = currentDevil?.name ?: "The Devil",
                                        onReadyForDeliberation = { viewModel.moveToDeliberation() }
                                    )
                                }

                                RoundPhase.DELIBERATION -> {
                                    DeliberationControls(
                                        devilName = currentDevil?.name ?: "The Devil",
                                        onProceedToJudgment = { viewModel.moveToJudgment() }
                                    )
                                }

                                RoundPhase.JUDGMENT -> {
                                    JudgmentControls(
                                        players = uiState.players,
                                        devilId = currentDevil?.id,
                                        onSelectWinner = { viewModel.selectRoundWinner(it) }
                                    )
                                }

                                RoundPhase.PUNISHMENT_DRAW,
                                RoundPhase.ROUND_SUMMARY -> {
                                    RoundSummaryControls(
                                        winner = uiState.roundWinner,
                                        onDrawPunishment = { viewModel.drawPunishmentForLoser() },
                                        onNextRound = { viewModel.nextRound() }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Bottom Player Scoreboard
            PlayerScoreBar(players = uiState.players)

            Spacer(modifier = Modifier.height(12.dp))
        }

        // Punishment Dialog
        if (uiState.showPunishmentDialog && uiState.currentPunishment != null) {
            DevilPunishmentDialog(
                punishment = uiState.currentPunishment!!,
                onDismiss = { viewModel.dismissPunishmentDialog() },
                onReroll = { viewModel.rerollPunishment() },
                onAccept = { viewModel.dismissPunishmentDialog() }
            )
        }

        // Quit Confirmation Dialog
        if (showQuitDialog) {
            QuitGameDialog(
                onDismiss = { showQuitDialog = false },
                onConfirmQuit = {
                    showQuitDialog = false
                    viewModel.navigateTo(AppScreen.HOME)
                }
            )
        }

        // History Dialog
        if (showHistoryDialog) {
            HistoryDialog(
                history = uiState.roundHistory,
                onDismiss = { showHistoryDialog = false }
            )
        }
    }
}

@Composable
private fun PromptRevealControls(
    devilName: String,
    onReadyForDeliberation: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = SinSurfaceVariant,
        border = BorderStroke(1.dp, SinBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "STEP 1: DEVIL READS & SINNERS PLAY",
                    color = DevilGold,
                    fontSize = 13.sp,
                    fontFamily = BebasNeueFontFamily,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$devilName reads the prompt aloud.\nAll other players choose their best physical answer card and place it face-down.",
                    color = SinWhite,
                    fontSize = 13.sp,
                    fontFamily = MontserratFontFamily,
                    textAlign = TextAlign.Center,
                    lineHeight = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = onReadyForDeliberation,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("all_cards_submitted_button"),
                colors = ButtonDefaults.buttonColors(containerColor = DevilCrimson),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = "ALL CARDS SUBMITTED ➔",
                    fontSize = 15.sp,
                    fontFamily = BebasNeueFontFamily,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun DeliberationControls(
    devilName: String,
    onProceedToJudgment: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = SinSurfaceVariant,
        border = BorderStroke(1.dp, DevilBlood)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "STEP 2: DELIBERATION OF HELL",
                    color = DevilCrimson,
                    fontSize = 14.sp,
                    fontFamily = BebasNeueFontFamily,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$devilName shuffles all submitted physical cards and reads each one out loud with dramatic flair!",
                    color = SinWhite,
                    fontSize = 13.sp,
                    fontFamily = MontserratFontFamily,
                    textAlign = TextAlign.Center,
                    lineHeight = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = onProceedToJudgment,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("ready_to_judge_button"),
                colors = ButtonDefaults.buttonColors(containerColor = DevilCrimson),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = "SELECT WINNING SINNER ➔",
                    fontSize = 15.sp,
                    fontFamily = BebasNeueFontFamily,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun JudgmentControls(
    players: List<Player>,
    devilId: String?,
    onSelectWinner: (Player) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = SinSurfaceVariant,
        border = BorderStroke(1.5.dp, DevilCrimson)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = "WHO PLAYED THE BEST CARD? 🔥",
                color = DevilGold,
                fontSize = 13.sp,
                fontFamily = BebasNeueFontFamily,
                letterSpacing = 1.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Non-Devil players list to pick winner
            val eligibleSinners = players.filterNot { it.id == devilId }

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(eligibleSinners, key = { it.id }) { sinner ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelectWinner(sinner) }
                            .testTag("select_winner_${sinner.id}"),
                        shape = RoundedCornerShape(8.dp),
                        color = SinBlack,
                        border = BorderStroke(1.dp, SinBorder)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = sinner.avatarEmoji, fontSize = 18.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = sinner.name,
                                    color = SinWhite,
                                    fontFamily = MontserratFontFamily,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Surface(
                                color = DevilCrimson,
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "AWARD SIN (+1)",
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontFamily = BebasNeueFontFamily,
                                    letterSpacing = 0.5.sp,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RoundSummaryControls(
    winner: Player?,
    onDrawPunishment: () -> Unit,
    onNextRound: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = SinSurfaceVariant,
        border = BorderStroke(1.5.dp, DevilGold)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "🔥", fontSize = 20.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "${winner?.name ?: "Sinner"} WON THE ROUND!",
                    color = DevilGold,
                    fontSize = 18.sp,
                    fontFamily = BebasNeueFontFamily,
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onDrawPunishment,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("draw_punishment_button"),
                    border = BorderStroke(1.2.dp, DevilBlood),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = DevilCrimson),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = "LOSER DARE 💀",
                        fontSize = 13.sp,
                        fontFamily = BebasNeueFontFamily,
                        letterSpacing = 0.5.sp
                    )
                }

                Button(
                    onClick = onNextRound,
                    modifier = Modifier
                        .weight(1.3f)
                        .testTag("next_round_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = DevilCrimson),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = "NEXT ROUND ➔",
                        fontSize = 15.sp,
                        fontFamily = BebasNeueFontFamily,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun GameOverCard(
    winner: Player,
    onPlayAgain: () -> Unit,
    onHome: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .testTag("game_over_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SinBlack),
        border = BorderStroke(2.dp, DevilGold)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "👑", fontSize = 54.sp)
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "HAIL THE SUPREME DEVIL!",
                color = DevilGold,
                fontSize = 28.sp,
                fontFamily = BebasNeueFontFamily,
                letterSpacing = 1.5.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "${winner.name} (${winner.score} Sins)",
                color = SinWhite,
                fontSize = 22.sp,
                fontFamily = MontserratFontFamily,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Conquered all physical cards and ruled Hell.",
                color = SinGrey,
                fontSize = 13.sp,
                fontFamily = MontserratFontFamily,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(26.dp))

            Button(
                onClick = onPlayAgain,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("play_again_button"),
                colors = ButtonDefaults.buttonColors(containerColor = DevilCrimson),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "PLAY AGAIN 🔥",
                    fontFamily = BebasNeueFontFamily,
                    fontSize = 18.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedButton(
                onClick = onHome,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                border = BorderStroke(1.dp, SinBorder),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = SinWhite),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "RETURN TO MAIN MENU",
                    fontFamily = BebasNeueFontFamily,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
private fun QuitGameDialog(onDismiss: () -> Unit, onConfirmQuit: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SinBlack),
            border = BorderStroke(1.5.dp, DevilCrimson)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "ABANDON GAME?",
                    color = DevilCrimson,
                    fontSize = 22.sp,
                    fontFamily = BebasNeueFontFamily,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Are you sure you want to quit the current Devil's Deck party session? Unsaved progress will be lost.",
                    color = SinGrey,
                    fontSize = 13.sp,
                    fontFamily = MontserratFontFamily,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(18.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        border = BorderStroke(1.dp, SinBorder),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("RESUME", color = SinWhite, fontFamily = BebasNeueFontFamily)
                    }
                    Button(
                        onClick = onConfirmQuit,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = DevilBlood),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("QUIT", color = Color.White, fontFamily = BebasNeueFontFamily)
                    }
                }
            }
        }
    }
}

@Composable
private fun HistoryDialog(
    history: List<com.example.data.model.RoundHistoryItem>,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SinBlack),
            border = BorderStroke(1.5.dp, DevilGold)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "ROUND LOG OF SINS",
                        color = DevilGold,
                        fontSize = 18.sp,
                        fontFamily = BebasNeueFontFamily,
                        letterSpacing = 1.sp
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = SinGrey)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (history.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No rounds recorded yet in this session.",
                            color = SinGrey,
                            fontSize = 12.sp,
                            fontFamily = MontserratFontFamily
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(260.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(history) { item ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = SinSurfaceVariant,
                                border = BorderStroke(1.dp, SinBorder)
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "ROUND ${item.roundNumber} (DEVIL: ${item.devilName})",
                                            color = DevilCrimson,
                                            fontSize = 11.sp,
                                            fontFamily = BebasNeueFontFamily
                                        )
                                        Text(
                                            text = "WINNER: ${item.winnerName}",
                                            color = DevilGold,
                                            fontSize = 11.sp,
                                            fontFamily = BebasNeueFontFamily
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = item.promptText,
                                        color = SinWhite,
                                        fontSize = 12.sp,
                                        fontFamily = MontserratFontFamily
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
