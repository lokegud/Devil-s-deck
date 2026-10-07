package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Player
import com.example.ui.theme.BebasNeueFontFamily
import com.example.ui.theme.DevilBlood
import com.example.ui.theme.DevilCrimson
import com.example.ui.theme.DevilGold
import com.example.ui.theme.MontserratFontFamily
import com.example.ui.theme.SinGrey
import com.example.ui.theme.SinWhite

@Composable
fun DevilHeader(
    currentDevil: Player?,
    roundNumber: Int,
    targetScore: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("devil_header_banner"),
        color = DevilBlood.copy(alpha = 0.5f),
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.5.dp, DevilCrimson)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "ROUND $roundNumber",
                        color = DevilGold,
                        fontSize = 13.sp,
                        fontFamily = BebasNeueFontFamily,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.size(6.dp))
                    Text(
                        text = "• FIRST TO $targetScore SINS WINS",
                        color = SinGrey,
                        fontSize = 11.sp,
                        fontFamily = MontserratFontFamily
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 2.dp)
                ) {
                    Text(
                        text = currentDevil?.avatarEmoji ?: "😈",
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.size(6.dp))
                    Text(
                        text = (currentDevil?.name ?: "THE DEVIL").uppercase(),
                        color = SinWhite,
                        fontSize = 18.sp,
                        fontFamily = BebasNeueFontFamily,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.size(6.dp))
                    Surface(
                        color = DevilCrimson,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "JUDGE / DEVIL",
                            color = Color.White,
                            fontSize = 9.sp,
                            fontFamily = BebasNeueFontFamily,
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Icon(
                imageVector = Icons.Default.LocalFireDepartment,
                contentDescription = "Devil Fire",
                tint = DevilCrimson,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}
