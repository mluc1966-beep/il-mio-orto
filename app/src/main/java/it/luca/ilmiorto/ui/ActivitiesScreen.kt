package it.luca.ilmiorto.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import it.luca.ilmiorto.data.GardenState
import it.luca.ilmiorto.data.cropName
import it.luca.ilmiorto.data.zoneName
import it.luca.ilmiorto.data.GardenTask
import it.luca.ilmiorto.data.TASK_CATEGORIES

@Composable
fun ActivitiesScreen(
    state: GardenState,
    onToggleTask: (String) -> Unit,
    onRemoveTask: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var categoryFilter by remember { mutableStateOf("Tutte") }
    val categories = listOf("Tutte") + TASK_CATEGORIES
    val tasks = state.tasks
        .filter { categoryFilter == "Tutte" || it.category == categoryFilter }
        .sortedWith(compareBy<GardenTask> { it.completed }.thenBy { it.date }.thenBy { it.title })

    Column(modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            SectionTitle(
                title = "Attività",
                subtitle = "Lavori programmati, irrigazioni, concimazioni e interventi sull’orto.",
            )
            ChoiceChips(
                choices = categories,
                selected = categoryFilter,
                onSelected = { categoryFilter = it },
            )
        }

        if (tasks.isEmpty()) {
            EmptyState(
                emoji = "✅",
                title = "Nessuna attività",
                message = "Usa il pulsante + per programmare il primo lavoro.",
                modifier = Modifier.padding(16.dp),
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                items(tasks, key = { it.id }) { task ->
                    TaskCard(
                        task = task,
                        zoneName = state.zoneName(task.zoneId),
                        cropName = state.cropName(task.cropId),
                        onToggle = { onToggleTask(task.id) },
                        onRemove = { onRemoveTask(task.id) },
                    )
                }
                item { Spacer(Modifier.height(72.dp)) }
            }
        }
    }
}

@Composable
fun TaskCard(
    task: GardenTask,
    zoneName: String,
    cropName: String,
    onToggle: () -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.Top,
        ) {
            Checkbox(checked = task.completed, onCheckedChange = { onToggle() })
            Column(Modifier.weight(1f).padding(top = 4.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        task.title,
                        style = MaterialTheme.typography.titleMedium,
                        textDecoration = if (task.completed) TextDecoration.LineThrough else null,
                        modifier = Modifier.weight(1f),
                    )
                    Text(
                        task.date,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    buildList {
                        add(task.category)
                        if (zoneName.isNotBlank()) add(zoneName)
                        if (cropName.isNotBlank()) add(cropName)
                    }.joinToString(" · "),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                if (task.notes.isNotBlank()) {
                    Spacer(Modifier.height(6.dp))
                    HorizontalDivider()
                    Spacer(Modifier.height(6.dp))
                    Text(task.notes, style = MaterialTheme.typography.bodyMedium)
                }
                TextButton(onClick = onRemove, modifier = Modifier.align(Alignment.End)) {
                    Text("Elimina")
                }
            }
        }
    }
}
