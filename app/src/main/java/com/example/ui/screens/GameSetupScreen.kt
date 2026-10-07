package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.example.data.model.DeckCategory
import com.example.ui.components.GrindBackground
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GameSetupScreen(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddPlayerDialog by remember { mutableStateOf(false) }

    BackHandler {
        viewModel.navigateTo(AppScreen.HOME)
    }

    GrindBackground(modifier = modifier) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top App Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { viewModel.navigateTo(AppScreen.HOME) },
                    modifier = Modifier.testTag("setup_back_button")
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
                        text = "GAME SETUP",
                        color = SinWhite,
                        fontSize = 24.sp,
                        fontFamily = BebasNeueFontFamily,
                        letterSpacing = 1.2.sp
                    )
                    Text(
                        text = uiState.gameMode.title.uppercase(),
                        color = DevilCrimson,
                        fontSize = 11.sp,
                        fontFamily = BebasNeueFontFamily,
                        letterSpacing = 1.sp
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Target Sins Selection
                item {
                    SectionHeader(title = "TARGET SINS TO WIN", emoji = "🔥")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(3, 5, 7, 10).forEach { score ->
                            val isSelected = uiState.targetSins == score
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { viewModel.setTargetSins(score) },
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) DevilBlood else SinSurfaceVariant,
                                border = BorderStroke(1.5.dp, if (isSelected) DevilCrimson else SinBorder)
                            ) {
                                Column(
                                    modifier = Modifier.padding(vertical = 10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "$score",
                                        color = if (isSelected) DevilGold else SinWhite,
                                        fontSize = 20.sp,
                                        fontFamily = BebasNeueFontFamily,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "SINS",
                                        color = if (isSelected) SinWhite else SinGrey,
                                        fontSize = 9.sp,
                                        fontFamily = BebasNeueFontFamily
                                    )
                                }
                            }
                        }
                    }
                }

                // Timer Selection
                item {
                    SectionHeader(title = "ROUND TIMER", emoji = "⏱️")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(0 to "OFF", 30 to "30s", 45 to "45s", 60 to "60s").forEach { (sec, label) ->
                            val isSelected = uiState.timerTotalSeconds == sec
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { viewModel.setTimerSeconds(sec) },
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) DevilBlood else SinSurfaceVariant,
                                border = BorderStroke(1.5.dp, if (isSelected) DevilCrimson else SinBorder)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = label,
                                        color = if (isSelected) DevilGold else SinWhite,
                                        fontSize = 15.sp,
                                        fontFamily = BebasNeueFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.sp
                                    )
                                }
                            }
                        }
                    }
                }

                // Decks / Categories Selection
                item {
                    SectionHeader(title = "ACTIVE CARD DECKS", emoji = "🃏")
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        DeckCategory.entries.forEach { category ->
                            val isSelected = uiState.selectedCategories.contains(category)
                            FilterChip(
                                selected = isSelected,
                                onClick = { viewModel.toggleCategory(category) },
                                label = {
                                    Text(
                                        text = "${category.emoji} ${category.title}",
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
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true,
                                    selected = isSelected,
                                    borderColor = SinBorder,
                                    selectedBorderColor = DevilCrimson
                                )
                            )
                        }
                    }
                }

                // Players List
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SectionHeader(title = "PLAYERS (${uiState.players.size})", emoji = "👥")
                        Button(
                            onClick = { showAddPlayerDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = DevilBlood),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, DevilCrimson),
                            modifier = Modifier.testTag("add_player_dialog_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add Player",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "ADD",
                                fontFamily = BebasNeueFontFamily,
                                fontSize = 13.sp,
                                color = Color.White
                            )
                        }
                    }
                }

                items(uiState.players, key = { it.id }) { player ->
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        color = SinSurface,
                        border = BorderStroke(1.dp, SinBorder)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = player.avatarEmoji,
                                    fontSize = 20.sp
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = player.name,
                                        color = SinWhite,
                                        fontSize = 14.sp,
                                        fontFamily = MontserratFontFamily,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    if (player.id == uiState.players.firstOrNull()?.id) {
                                        Text(
                                            text = "FIRST DEVIL 👑",
                                            color = DevilGold,
                                            fontSize = 9.sp,
                                            fontFamily = BebasNeueFontFamily,
                                            letterSpacing = 0.5.sp
                                        )
                                    }
                                }
                            }

                            if (uiState.players.size > 2) {
                                IconButton(
                                    onClick = { viewModel.removePlayer(player.id) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Remove Player",
                                        tint = SinGrey,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            // Bottom Start Button
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = SinBlack,
                border = BorderStroke(1.dp, SinBorder)
            ) {
                Box(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Button(
                        onClick = { viewModel.startGame() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .testTag("launch_game_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = DevilCrimson),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.5.dp, Color.White)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocalFireDepartment,
                                contentDescription = "Start Fire",
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "ENTER THE HELLFIRE (DEAL PROMPT) 🔥",
                                color = Color.White,
                                fontSize = 17.sp,
                                fontFamily = BebasNeueFontFamily,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.2.sp
                            )
                        }
                    }
                }
            }
        }

        // Add Player Dialog
        if (showAddPlayerDialog) {
            AddPlayerDialog(
                onDismiss = { showAddPlayerDialog = false },
                onAdd = { name, emoji ->
                    viewModel.addPlayer(name, emoji)
                    showAddPlayerDialog = false
                }
            )
        }
    }
}

@Composable
private fun SectionHeader(title: String, emoji: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = emoji, fontSize = 14.sp)
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = title,
            color = DevilGold,
            fontSize = 14.sp,
            fontFamily = BebasNeueFontFamily,
            letterSpacing = 1.sp
        )
    }
}

@Composable
private fun AddPlayerDialog(
    onDismiss: () -> Unit,
    onAdd: (name: String, emoji: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var selectedEmoji by remember { mutableStateOf("😈") }
    val emojiOptions = listOf("😈", "💀", "🍸", "🔥", "👑", "🐍", "🎲", "🕷️", "🍷", "🖤")

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
                    text = "ADD A SINNER",
                    color = DevilCrimson,
                    fontSize = 22.sp,
                    fontFamily = BebasNeueFontFamily,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Player Name") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DevilCrimson,
                        unfocusedBorderColor = SinBorder,
                        focusedTextColor = SinWhite,
                        unfocusedTextColor = SinWhite
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("player_name_input")
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "CHOOSE AVATAR",
                    color = SinGrey,
                    fontSize = 11.sp,
                    fontFamily = BebasNeueFontFamily,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    emojiOptions.take(5).forEach { emoji ->
                        EmojiOption(emoji, selected = selectedEmoji == emoji) { selectedEmoji = emoji }
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    emojiOptions.drop(5).forEach { emoji ->
                        EmojiOption(emoji, selected = selectedEmoji == emoji) { selectedEmoji = emoji }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = SinSurfaceVariant),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("CANCEL", color = SinGrey, fontFamily = BebasNeueFontFamily)
                    }

                    Button(
                        onClick = { if (name.isNotBlank()) onAdd(name, selectedEmoji) },
                        colors = ButtonDefaults.buttonColors(containerColor = DevilCrimson),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("confirm_add_player_button"),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("ADD 🔥", color = Color.White, fontFamily = BebasNeueFontFamily)
                    }
                }
            }
        }
    }
}

@Composable
private fun EmojiOption(emoji: String, selected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .size(38.dp)
            .clickable { onClick() },
        shape = CircleShape,
        color = if (selected) DevilBlood else SinSurfaceVariant,
        border = BorderStroke(1.5.dp, if (selected) DevilCrimson else Color.Transparent)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(text = emoji, fontSize = 18.sp)
        }
    }
}
