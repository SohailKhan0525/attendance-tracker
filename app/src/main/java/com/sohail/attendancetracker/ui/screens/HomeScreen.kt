package com.sohail.attendancetracker.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sohail.attendancetracker.data.model.AttendanceStatus
import com.sohail.attendancetracker.data.model.OverallStats
import com.sohail.attendancetracker.data.model.SubjectStats
import com.sohail.attendancetracker.ui.theme.CriticalRed
import com.sohail.attendancetracker.ui.theme.SafeGreen
import com.sohail.attendancetracker.ui.theme.WarningOrange
import com.sohail.attendancetracker.viewmodel.HomeViewModel

/**
 * Home dashboard screen showing overall and subject-wise attendance
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onSubjectClick: (String) -> Unit
) {
    val overallStats by viewModel.overallStats.collectAsState()
    val subjectStats by viewModel.subjectStats.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dashboard") },
                actions = {
                    IconButton(onClick = { viewModel.refresh() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                }
            )
        }
    ) { padding ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Overall stats card
                item {
                    overallStats?.let { stats ->
                        OverallStatsCard(stats)
                    }
                }
                
                // Subject stats header
                item {
                    Text(
                        text = "Subject-wise Attendance",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                // Subject list
                items(subjectStats) { subject ->
                    SubjectStatsCard(
                        subject = subject,
                        onClick = { onSubjectClick(subject.subjectName) }
                    )
                }
            }
        }
    }
}

@Composable
private fun OverallStatsCard(stats: OverallStats) {
    val statusColor by animateColorAsState(
        targetValue = when (stats.status) {
            AttendanceStatus.SAFE -> SafeGreen
            AttendanceStatus.WARNING -> WarningOrange
            AttendanceStatus.CRITICAL -> CriticalRed
        },
        label = "statusColor"
    )
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Overall Attendance",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                if (stats.status == AttendanceStatus.WARNING || stats.status == AttendanceStatus.CRITICAL) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Warning",
                        tint = statusColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Big percentage display
            Text(
                text = "${String.format("%.1f", stats.percentage)}%",
                style = MaterialTheme.typography.displayLarge,
                fontWeight = FontWeight.Bold,
                color = statusColor
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Progress bar
            LinearProgressIndicator(
                progress = { (stats.percentage / 100f).coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = statusColor,
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Stats details
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatsItem(
                    label = "Attended",
                    value = stats.attendedPeriods.toString()
                )
                StatsItem(
                    label = "Total",
                    value = stats.totalPeriods.toString()
                )
                StatsItem(
                    label = "Safe Bunks",
                    value = stats.safeBunksRemaining.toString()
                )
            }
        }
    }
}

@Composable
private fun SubjectStatsCard(
    subject: SubjectStats,
    onClick: () -> Unit
) {
    val statusColor = when {
        subject.percentage >= 75 -> SafeGreen
        subject.percentage >= 72 -> WarningOrange
        else -> CriticalRed
    }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = subject.subjectName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "${subject.attendedClasses} / ${subject.totalClasses} classes",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                LinearProgressIndicator(
                    progress = { (subject.percentage / 100f).coerceIn(0f, 1f) },
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = statusColor,
                )
            }
            
            Text(
                text = "${String.format("%.1f", subject.percentage)}%",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = statusColor
            )
        }
    }
}

@Composable
private fun StatsItem(
    label: String,
    value: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
