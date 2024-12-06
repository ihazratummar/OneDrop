package com.hazrat.onedrop.auth.presentation

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlin.math.sin


/**
 * @author Hazrat Ummar Shaikh
 * Created on 05-12-2024
 */


@Composable
fun HeartbeatAnimation() {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val phaseShift by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing)
        ), label = ""
    )

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(top = 100.dp)
    ) {
        val width = size.width
        val height = size.height / 2

        val path = Path()

        // Drawing heartbeat graph
        for (x in 0 until width.toInt()) {
            val normalizedX = x / width
            val sineWaveY =
                (sin(2 * Math.PI * (normalizedX + phaseShift)) * height / 4).toFloat()
            val y = height + sineWaveY

            if (x == 0) {
                path.moveTo(x.toFloat(), y)
            } else {
                path.lineTo(x.toFloat(), y)
            }
        }

        drawPath(
            path = path,
            color = Color.Red,
            style = Stroke(width = 4.dp.toPx())
        )
    }
}

@Composable
fun BloodFlowAnimation() {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val waveOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = LinearEasing)
        ), label = ""
    )

    Box() {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .align(Alignment.BottomCenter)
        ) {
            val width = size.width
            val height = size.height

            // Draw flowing blood effect
            for (x in 0 until width.toInt() step 20) {
                val normalizedX = (x / width + waveOffset) % 1
                val y = height / 2 + (sin(2 * Math.PI * normalizedX) * height / 8).toFloat()

                drawCircle(
                    color = Color.Red,
                    radius = 10f,
                    center = androidx.compose.ui.geometry.Offset(x.toFloat(), y)
                )
            }
        }
    }

}