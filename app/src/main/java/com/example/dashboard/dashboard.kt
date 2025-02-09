package com.example.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.Brush
import kotlin.random.Random
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun DashboardScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFBF8EF))
            .padding(16.dp)
    ) {
        ContentArea()
        UsageChartContainer()
    }
}

@Composable
fun ContentArea() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 64.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // First row of cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DashboardCard(
                title = "Total Devices",
                value = "50 Devices",
                modifier = Modifier.weight(1f)
            )
            DashboardCard(
                title = "Active Devices",
                value = "42 Devices",
                modifier = Modifier.weight(1f)
            )
        }

        // Second row of cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DashboardCard(
                title = "Issues",
                value = "20 Issues",
                modifier = Modifier.weight(1f)
            )
            DashboardCard(
                title = "Alerts",
                value = "5 Alerts",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp)) // Spacing before the graph
    }
}

@Composable
fun DashboardCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFC9E6F0)),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(title)
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}
@Composable
fun UsageChartContainer() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Device Usage Over Time",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                UsageChart()
            }
        }
    }
}

@Composable
fun UsageChart() {
    val points = remember {
        List(7) {
            Random.nextFloat() * 0.8f + 0.2f // Random values between 0.2 and 1.0
        }
    }

    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {
        val width = size.width
        val height = size.height
        val path = Path()
        val strokePath = Path()

        // Calculate points
        val spaceBetweenPoints = width / (points.size - 1)
        val coordinates = points.mapIndexed { index, point ->
            Offset(
                x = index * spaceBetweenPoints,
                y = height - (point * height)
            )
        }

        // Create smooth curve
        path.moveTo(coordinates.first().x, coordinates.first().y)
        strokePath.moveTo(coordinates.first().x, coordinates.first().y)

        for (i in 0 until coordinates.size - 1) {
            val current = coordinates[i]
            val next = coordinates[i + 1]

            val controlPoint1 = Offset(
                x = current.x + (next.x - current.x) / 2f,
                y = current.y
            )
            val controlPoint2 = Offset(
                x = current.x + (next.x - current.x) / 2f,
                y = next.y
            )

            path.cubicTo(
                controlPoint1.x, controlPoint1.y,
                controlPoint2.x, controlPoint2.y,
                next.x, next.y
            )
            strokePath.cubicTo(
                controlPoint1.x, controlPoint1.y,
                controlPoint2.x, controlPoint2.y,
                next.x, next.y
            )
        }

        // Create gradient path
        path.lineTo(width, height)
        path.lineTo(0f, height)
        path.close()

        // Draw gradient fill
        drawPath(
            path = path,
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF6200EE).copy(alpha = 0.4f),
                    Color(0xFF6200EE).copy(alpha = 0.1f)
                )
            )
        )

        // Draw line
        drawPath(
            path = strokePath,
            color = Color(0xFF6200EE),
            style = Stroke(
                width = 4.dp.toPx(),
                cap = StrokeCap.Round
            )
        )

        // Draw points
        coordinates.forEach { point ->
            drawCircle(
                color = Color(0xFF6200EE),
                radius = 6.dp.toPx(),
                center = point
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewDashboard() {
    DashboardScreen()
}
