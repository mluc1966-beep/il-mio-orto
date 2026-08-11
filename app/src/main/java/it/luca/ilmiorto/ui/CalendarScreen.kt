package it.luca.ilmiorto.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import it.luca.ilmiorto.data.GardenState
import it.luca.ilmiorto.data.zoneName
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CalendarScreen(
    state: GardenState,
    modifier: Modifier = Modifier,
) {
    val sortedTasks = state.tasks.sortedBy { it.date }
    val grouped = sortedTasks.groupBy { it.date }
    val pending = state.tasks.count { !it.completed }
    val completed = state.tasks.count { it.completed }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item {
            SectionTitle(
                title = "Calendario",
                subtitle = "Agenda cronologica delle attività registrate.",
            )
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                StatCard(pending.toString(), "da fare", Modifier.weight(1f))
                StatCard(completed.toString(), "completate", Modifier.weight(1f))
                StatCard(grouped.size.toString(), "giorni", Modifier.weight(1f))
            }
        }

        if (grouped.isEmpty()) {
            item {
                EmptyState(
                    emoji = "📅",
                    title = "Calendario vuoto",
                    message = "Le attività aggiunte compariranno qui raggruppate per giorno.",
                )
            }
        } else {
            items(grouped.entries.toList(), key = { it.key }) { entry ->
                CalendarDayCard(
                    date = entry.key,
                    state = state,
                    taskIds = entry.value.map { it.id },
                )
            }
        }
        item { Spacer(Modifier.height(72.dp)) }
    }
}

@Composable
private fun CalendarDayCard(
    date: String,
    state: GardenState,
    taskIds: List<String>,
) {
    val parsed = runCatching { LocalDate.parse(date) }.getOrNull()
    val prettyDate = parsed?.let {
        val weekday = it.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ITALIAN)
        val month = it.month.getDisplayName(TextStyle.FULL, Locale.ITALIAN)
        "${weekday.replaceFirstChar { ch -> if (ch.isLowerCase()) ch.titlecase(Locale.ITALIAN) else ch.toString() }}, ${it.dayOfMonth} $month ${it.year}"
    } ?: date
    val tasks = state.tasks.filter { it.id in taskIds }

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(prettyDate, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.height(10.dp))
            tasks.forEachIndexed { index, task ->
                Row(Modifier.fillMaxWidth()) {
                    Text(
                        if (task.completed) "✓" else "•",
                        modifier = Modifier.padding(end = 10.dp),
                        fontWeight = FontWeight.Bold,
                        color = if (task.completed) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                    )
                    Column(Modifier.weight(1f)) {
                        Text(task.title, style = MaterialTheme.typography.bodyLarge)
                        Text(
                            buildList {
                                add(task.category)
                                state.zoneName(task.zoneId).takeIf { it.isNotBlank() }?.let(::add)
                            }.joinToString(" · "),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
                if (index < tasks.lastIndex) Spacer(Modifier.height(10.dp))
            }
        }
    }
}
