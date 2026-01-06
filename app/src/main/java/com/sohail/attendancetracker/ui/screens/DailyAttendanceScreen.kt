package com.sohail.attendancetracker.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sohail.attendancetracker.data.model.Subjects
import com.sohail.attendancetracker.viewmodel.DailyAttendanceViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Screen for daily attendance entry
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyAttendanceScreen(
    viewModel: DailyAttendanceViewModel,
    onNavigateBack: () -> Unit
) {
    val selectedDate by viewModel.selectedDate.collectAsState()
    val periodSubjects by viewModel.periodSubjects.collectAsState()
    val periodAttendance by viewModel.periodAttendance.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isSaved by viewModel.isSaved.collectAsState()
    
    var showSaveDialog by remember { mutableStateOf(false) }
    var showClearDialog by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Daily Attendance") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showClearDialog = true }) {
                        Icon(Icons.Default.Delete, "Clear")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Date selector
            item {
                DateSelectorCard(
                    selectedDate = selectedDate,
                    onPreviousDay = { viewModel.goToPreviousDay() },
                    onNextDay = { viewModel.goToNextDay() },
                    onToday = { viewModel.goToToday() }
                )
            }
            
            // Status indicator
            item {
                AnimatedVisibility(visible = isSaved) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.secondary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Attendance saved for this date",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
            
            // Period entries
            items(6) { index ->
                val period = index + 1
                PeriodCard(
                    period = period,
                    selectedSubject = periodSubjects[period],
                    isPresent = periodAttendance[period],
                    onSubjectChange = { subject ->
                        viewModel.setPeriodSubject(period, subject)
                    },
                    onAttendanceChange = { isPresent ->
                        viewModel.setPeriodAttendance(period, isPresent)
                    }
                )
            }
            
            // Save button
            item {
                Button(
                    onClick = { showSaveDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = !isLoading && periodSubjects.isNotEmpty()
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Icon(Icons.Default.Save, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Save Attendance")
                    }
                }
            }
        }
    }
    
    // Save confirmation dialog
    if (showSaveDialog) {
        AlertDialog(
            onDismissRequest = { showSaveDialog = false },
            title = { Text("Save Attendance") },
            text = { Text("Save attendance for ${selectedDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))}?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.saveAttendance { success ->
                            if (success) {
                                showSaveDialog = false
                            }
                        }
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSaveDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
    
    // Clear confirmation dialog
    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text("Clear Attendance") },
            text = { Text("Clear all attendance data for this date?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.clearDate()
                        showClearDialog = false
                    }
                ) {
                    Text("Clear", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun DateSelectorCard(
    selectedDate: LocalDate,
    onPreviousDay: () -> Unit,
    onNextDay: () -> Unit,
    onToday: () -> Unit
) {
    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onPreviousDay) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Previous day")
                }
                
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = selectedDate.format(DateTimeFormatter.ofPattern("EEEE")),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = selectedDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy")),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                IconButton(onClick = onNextDay) {
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, "Next day")
                }
            }
            
            if (selectedDate != LocalDate.now()) {
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(
                    onClick = onToday,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Go to Today")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PeriodCard(
    period: Int,
    selectedSubject: String?,
    isPresent: Boolean?,
    onSubjectChange: (String) -> Unit,
    onAttendanceChange: (Boolean) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    
    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Period $period",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Subject selector
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it }
            ) {
                OutlinedTextField(
                    value = selectedSubject ?: "Select Subject",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Subject") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    colors = OutlinedTextFieldDefaults.colors()
                )
                
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    Subjects.allSubjects.forEach { subject ->
                        DropdownMenuItem(
                            text = { Text(subject.name) },
                            onClick = {
                                onSubjectChange(subject.name)
                                expanded = false
                            }
                        )
                    }
                }
            }
            
            // Attendance buttons
            if (selectedSubject != null) {
                Spacer(modifier = Modifier.height(12.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = isPresent == true,
                        onClick = { onAttendanceChange(true) },
                        label = { Text("Present") },
                        leadingIcon = if (isPresent == true) {
                            { Icon(Icons.Default.Check, contentDescription = null) }
                        } else null,
                        modifier = Modifier.weight(1f),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    )
                    
                    FilterChip(
                        selected = isPresent == false,
                        onClick = { onAttendanceChange(false) },
                        label = { Text("Absent") },
                        leadingIcon = if (isPresent == false) {
                            { Icon(Icons.Default.Close, contentDescription = null) }
                        } else null,
                        modifier = Modifier.weight(1f),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    )
                }
            }
        }
    }
}
