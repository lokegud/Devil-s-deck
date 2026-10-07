package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.PunishmentCard
import com.example.ui.theme.BebasNeueFontFamily
import com.example.ui.theme.DevilBlood
import com.example.ui.theme.DevilCrimson
import com.example.ui.theme.DevilGold
import com.example.ui.theme.DevilRed
import com.example.ui.theme.MontserratFontFamily
import com.example.ui.theme.SinBlack
import com.example.ui.theme.SinBorder
import com.example.ui.theme.SinGrey
import com.example.ui.theme.SinWhite

@Composable
fun DevilPunishmentDialog(
    punishment: PunishmentCard,
    onDismiss: () -> Unit,
    onReroll: () -> Unit,
    onAccept: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("punishment_dialog"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = SinBlack),
            border = BorderStroke(2.dp, DevilCrimson)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top row with close
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = DevilBlood,
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, DevilRed)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = punishment.severity.emoji,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.size(4.dp))
                            Text(
                                text = punishment.severity.label.uppercase(),
                                color = Color.White,
                                fontSize = 11.sp,
                                fontFamily = BebasNeueFontFamily,
                                letterSpacing = 1.sp
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = SinGrey
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "DEVIL'S PUNISHMENT",
                    color = DevilCrimson,
                    fontSize = 24.sp,
                    fontFamily = BebasNeueFontFamily,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )

                Text(
                    text = "Penalty for the round's worst answer or loser",
                    color = SinGrey,
                    fontSize = 12.sp,
                    fontFamily = MontserratFontFamily,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(18.dp))

                // The Dare Box
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    color = Color(0xFF191215),
                    border = BorderStroke(1.5.dp, DevilBlood)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = punishment.title.uppercase(),
                            color = DevilGold,
                            fontSize = 18.sp,
                            fontFamily = BebasNeueFontFamily,
                            letterSpacing = 1.sp,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = punishment.description,
                            color = SinWhite,
                            fontSize = 15.sp,
                            fontFamily = MontserratFontFamily,
                            fontWeight = FontWeight.Medium,
                            lineHeight = 22.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onReroll,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("punishment_reroll_button"),
                        border = BorderStroke(1.dp, SinBorder),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = SinWhite),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Reroll",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.size(6.dp))
                        Text(
                            text = "REROLL",
                            fontFamily = BebasNeueFontFamily,
                            fontSize = 14.sp
                        )
                    }

                    Button(
                        onClick = onAccept,
                        modifier = Modifier
                            .weight(1.2f)
                            .testTag("punishment_accept_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = DevilCrimson),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "INFLICT DARE 🔥",
                            fontFamily = BebasNeueFontFamily,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
