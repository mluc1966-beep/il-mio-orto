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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import it.luca.ilmiorto.data.DEFAULT_ZONES
import it.luca.ilmiorto.data.GardenState
import java.util.Locale

@Composable
fun ArchiveScreen(
    state: GardenState,
    onExport: () -> Unit,
    onImport: () -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showResetDialog by remember { mutableStateOf(false) }
    val completedTasks = state.tasks.count { it.completed }
    val totalHarvest = state.harvests.sumOf { it.weightGrams }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        SectionTitle(
            title = "Archivio",
            subtitle = "Riepilogo della stagione ${state.seasonYear} e gestione dei dati.",
        )

        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)),
        ) {
            Column(Modifier.padding(18.dp)) {
                Text("Stagione ${state.seasonYear}", style = MaterialTheme.typography.headlineSmall)
                Spacer(Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    StatCard(state.crops.size.toString(), "colture", Modifier.weight(1f))
                    StatCard(state.tasks.size.toString(), "attività", Modifier.weight(1f))
                    StatCard(state.harvests.size.toString(), "raccolti", Modifier.weight(1f))
                }
                Spacer(Modifier.height(12.dp))
                InfoLine("Attività completate", "$completedTasks/${state.tasks.size}")
                Spacer(Modifier.height(6.dp))
                InfoLine(
                    "Peso raccolto",
                    if (totalHarvest >= 1000) String.format(Locale.ITALY, "%.2f kg", totalHarvest / 1000.0) else "$totalHarvest g",
                )
                Spacer(Modifier.height(6.dp))
                InfoLine(
                    "Superficie censita",
                    String.format(Locale.ITALY, "%.1f m²", DEFAULT_ZONES.sumOf { it.areaSquareMeters }),
                )
            }
        }

        Card {
            Column(Modifier.padding(16.dp)) {
                Text("Backup", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(6.dp))
                Text(
                    "Esporta tutte le colture, attività e raccolte in un file JSON leggibile e reimportabile.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(Modifier.height(14.dp))
                Button(onClick = onExport, modifier = Modifier.fillMaxWidth()) {
                    Text("Esporta backup")
                }
                Spacer(Modifier.height(8.dp))
                OutlinedButton(onClick = onImport, modifier = Modifier.fillMaxWidth()) {
                    Text("Importa backup")
                }
            }
        }

        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.55f)),
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("Ripristino", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(4.dp))
                Text(
                    "Cancella i dati della stagione presenti sul telefono. È consigliabile esportare prima un backup.",
                    style = MaterialTheme.typography.bodyMedium,
                )
                Spacer(Modifier.height(10.dp))
                OutlinedButton(onClick = { showResetDialog = true }, modifier = Modifier.fillMaxWidth()) {
                    Text("Cancella tutti i dati")
                }
            }
        }

        Text(
            "Versione prototipo 0.1 · dati salvati solo sul dispositivo",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Cancellare tutti i dati?") },
            text = { Text("L’operazione rimuove colture, attività e raccolti registrati.") },
            confirmButton = {
                TextButton(onClick = {
                    showResetDialog = false
                    onReset()
                }) { Text("Cancella") }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) { Text("Annulla") }
            },
        )
    }
}
