package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.components.GrindBackground
import com.example.ui.theme.BebasNeueFontFamily
import com.example.ui.theme.DevilBlood
import com.example.ui.theme.DevilCrimson
import com.example.ui.theme.DevilGold
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
fun CustomCardsScreen(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    val customCards by viewModel.customCards.collectAsState()
    var showCreateDialog by remember { mutableStateOf(false) }

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
                    modifier = Modifier.testTag("custom_cards_back_button")
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
                        text = "HOUSE SINS & CUSTOM CARDS",
                        color = SinWhite,
                        fontSize = 20.sp,
                        fontFamily = BebasNeueFontFamily,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Create inside-joke prompts and custom punishments",
                        color = SinGrey,
                        fontSize = 11.sp,
                        fontFamily = MontserratFontFamily
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Add Button
            Button(
                onClick = { showCreateDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("add_custom_card_button"),
                colors = ButtonDefaults.buttonColors(containerColor = DevilCrimson),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add", tint = Color.White)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "CREATE NEW CUSTOM CARD 🔥",
                    fontFamily = BebasNeueFontFamily,
                    fontSize = 16.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            if (customCards.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "✍️", fontSize = 42.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "NO HOUSE SINS YET",
                            color = DevilGold,
                            fontFamily = BebasNeueFontFamily,
                            fontSize = 20.sp
                        )
                        Text(
                            text = "Add your own wild inside-jokes and taboo prompts!\nThey will automatically appear in your games.",
                            color = SinGrey,
                            fontFamily = MontserratFontFamily,
                            fontSize = 12.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(customCards, key = { it.id }) { card ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("custom_card_item_${card.id}"),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = SinSurface),
                            border = BorderStroke(1.2.dp, DevilBlood)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Surface(
                                            color = DevilCrimson,
                                            shape = RoundedCornerShape(4.dp)
                                        ) {
                                            Text(
                                                text = if (card.isPunishment) "LOSER DARE" else "PICK ${card.pickCount}",
                                                color = Color.White,
                                                fontSize = 9.sp,
                                                fontFamily = BebasNeueFontFamily,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "HOUSE SIN",
                                            color = DevilGold,
                                            fontSize = 10.sp,
                                            fontFamily = BebasNeueFontFamily
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = card.text,
                                        color = SinWhite,
                                        fontSize = 15.sp,
                                        fontFamily = MontserratFontFamily,
                                        fontWeight = FontWeight.SemiBold,
                                        lineHeight = 20.sp
                                    )
                                }

                                IconButton(
                                    onClick = { viewModel.deleteCustomCard(card.id) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete",
                                        tint = SinGrey
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        if (showCreateDialog) {
            CreateCustomCardDialog(
                onDismiss = { showCreateDialog = false },
                onSave = { text, pickCount, isPunishment ->
                    viewModel.saveCustomCard(text, pickCount, "CUSTOM", isPunishment)
                    showCreateDialog = false
                }
            )
        }
    }
}

@Composable
private fun CreateCustomCardDialog(
    onDismiss: () -> Unit,
    onSave: (text: String, pickCount: Int, isPunishment: Boolean) -> Unit
) {
    var text by remember { mutableStateOf("") }
    var pickCount by remember { mutableStateOf(1) }
    var isPunishment by remember { mutableStateOf(false) }

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
                    text = "CREATE CUSTOM CARD",
                    color = DevilCrimson,
                    fontSize = 20.sp,
                    fontFamily = BebasNeueFontFamily,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Prompt text (use '_____' for blank)") },
                    placeholder = { Text("The real reason I got fired: _____.") },
                    minLines = 3,
                    maxLines = 5,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DevilCrimson,
                        unfocusedBorderColor = SinBorder,
                        focusedTextColor = SinWhite,
                        unfocusedTextColor = SinWhite
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("custom_card_input")
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "CARD TYPE:",
                        color = SinGrey,
                        fontSize = 11.sp,
                        fontFamily = BebasNeueFontFamily
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Surface(
                            modifier = Modifier.clickable { isPunishment = false },
                            shape = RoundedCornerShape(6.dp),
                            color = if (!isPunishment) DevilBlood else SinSurfaceVariant,
                            border = BorderStroke(1.dp, if (!isPunishment) DevilCrimson else SinBorder)
                        ) {
                            Text(
                                text = "PROMPT",
                                color = SinWhite,
                                fontSize = 11.sp,
                                fontFamily = BebasNeueFontFamily,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        Surface(
                            modifier = Modifier.clickable { isPunishment = true },
                            shape = RoundedCornerShape(6.dp),
                            color = if (isPunishment) DevilBlood else SinSurfaceVariant,
                            border = BorderStroke(1.dp, if (isPunishment) DevilCrimson else SinBorder)
                        ) {
                            Text(
                                text = "LOSER DARE",
                                color = SinWhite,
                                fontSize = 11.sp,
                                fontFamily = BebasNeueFontFamily,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                if (!isPunishment) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "ANSWERS REQUIRED:",
                            color = SinGrey,
                            fontSize = 11.sp,
                            fontFamily = BebasNeueFontFamily
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf(1, 2).forEach { pick ->
                                Surface(
                                    modifier = Modifier.clickable { pickCount = pick },
                                    shape = RoundedCornerShape(6.dp),
                                    color = if (pickCount == pick) DevilBlood else SinSurfaceVariant,
                                    border = BorderStroke(1.dp, if (pickCount == pick) DevilCrimson else SinBorder)
                                ) {
                                    Text(
                                        text = "PICK $pick",
                                        color = SinWhite,
                                        fontSize = 11.sp,
                                        fontFamily = BebasNeueFontFamily,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
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
                        onClick = {
                            if (text.isNotBlank()) {
                                onSave(text, pickCount, isPunishment)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = DevilCrimson),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("save_custom_card_button"),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("SAVE 🔥", color = Color.White, fontFamily = BebasNeueFontFamily)
                    }
                }
            }
        }
    }
}
