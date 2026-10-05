package com.example.listanddetails.ui.details.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listanddetails.ui.theme.LiveIndicatorGreen
import kotlinx.coroutines.delay
import java.time.Instant
import kotlin.time.Duration.Companion.milliseconds

@SuppressLint("DefaultLocale")
@Composable
fun TMinusCountdownBadge(
    rawDate: String?,
    modifier: Modifier = Modifier
) {
    var currentTime by remember { mutableLongStateOf(System.currentTimeMillis()) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L.milliseconds)
            currentTime = System.currentTimeMillis()
        }
    }

    val targetMillis = remember(rawDate) {
        try {
            rawDate?.let { Instant.parse(it).toEpochMilli() } ?: 0L
        } catch (_: Exception) {
            0L
        }
    }

    val diffSeconds = (targetMillis - currentTime) / 1000

    if (diffSeconds > 0) {
        val days = diffSeconds / 86400
        val hours = (diffSeconds % 86400) / 3600
        val minutes = (diffSeconds % 3600) / 60
        val seconds = diffSeconds % 60

        Surface(
            color = Color.Black.copy(alpha = 0.65f),
            shape = RoundedCornerShape(8.dp),
            modifier = modifier.padding(bottom = 4.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(LiveIndicatorGreen)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = String.format("T - %02dd %02dh %02dm %02ds", days, hours, minutes, seconds),
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = LiveIndicatorGreen
                )
            }
        }
    }
}