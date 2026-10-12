package com.example.listanddetails.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun RocketLoadingIndicator(
    modifier: Modifier = Modifier,
    size: Dp = 150.dp,
    orbitRadius: Dp = 52.dp,
    label: String = "Связь с космодромом..."
) {
    val infiniteTransition = rememberInfiniteTransition(label = "RocketOrbitTransition")

    // Бесконечное вращение угла от 0 до 360 градусов
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "OrbitAngle"
    )

    // Легкая пульсация свечения центральной планеты
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.45f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1100, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "PulseAlpha"
    )

    val primaryColor = MaterialTheme.colorScheme.primary
    val outlineColor = MaterialTheme.colorScheme.outlineVariant

    val density = LocalDensity.current
    val orbitRadiusPx = with(density) { orbitRadius.toPx() }

    // Расчет координат ракеты на круговой орбите
    val radians = Math.toRadians(angle.toDouble())
    val rocketX = (orbitRadiusPx * cos(radians)).toFloat()
    val rocketY = (orbitRadiusPx * sin(radians)).toFloat()

    // Векторная иконка RocketLaunch изначально наклонена под 45 градусов.
    // Смещение +135 градусов ориентирует ракету строго по касательной полета по часовой стрелке.
    val rocketRotation = angle + 135f

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.size(size),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(size)) {
                val centerOffset = Offset(this.size.width / 2f, this.size.height / 2f)

                // 1. Пунктирная траектория орбиты
                drawCircle(
                    color = outlineColor.copy(alpha = 0.4f),
                    radius = orbitRadiusPx,
                    center = centerOffset,
                    style = Stroke(
                        width = 1.5.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                    )
                )

                // 2. Огненный шлейф за ракетой (светящаяся дуга)
                drawArc(
                    color = primaryColor.copy(alpha = 0.65f),
                    startAngle = angle - 70f,
                    sweepAngle = 65f,
                    useCenter = false,
                    topLeft = Offset(centerOffset.x - orbitRadiusPx, centerOffset.y - orbitRadiusPx),
                    size = Size(orbitRadiusPx * 2, orbitRadiusPx * 2),
                    style = Stroke(
                        width = 3.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                )

                // 3. Центральная мини-планета с ореолом
                drawCircle(
                    color = primaryColor.copy(alpha = pulseAlpha),
                    radius = 16.dp.toPx(),
                    center = centerOffset
                )
                drawCircle(
                    color = primaryColor,
                    radius = 9.dp.toPx(),
                    center = centerOffset
                )
            }

            // Сама ракета, смещенная на радиус орбиты и повернутая по траектории
            Icon(
                imageVector = Icons.Default.RocketLaunch,
                contentDescription = null,
                tint = primaryColor,
                modifier = Modifier
                    .size(28.dp)
                    .graphicsLayer {
                        translationX = rocketX
                        translationY = rocketY
                        rotationZ = rocketRotation
                    }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}