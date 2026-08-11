package it.luca.ilmiorto.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import it.luca.ilmiorto.data.COMPANION_RULES
import it.luca.ilmiorto.data.CROP_CATALOG
import it.luca.ilmiorto.data.companionSource

@Composable
fun CompanionScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var selectedId by remember { mutableStateOf("pomodoro") }
    val selected = CROP_CATALOG.firstOrNull { it.id == selectedId } ?: CROP_CATALOG.first()
    val rules = COMPANION_RULES.filter {
        it.firstCatalogId == selected.id || it.secondCatalogId == selected.id
    }

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("Consociazioni", style = MaterialTheme.typography.headlineSmall)
        Text(
            "Abbinamenti, disposizione suggerita, livello delle prove e fonti consultabili.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Text("Pianta principale", style = MaterialTheme.typography.titleSmall)
        CROP_CATALOG.chunked(3).forEach { rowItems ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                rowItems.forEach { item ->
                    FilterChip(
                        selected = item.id == selected.id,
                        onClick = { selectedId = item.id },
                        label = { Text("${item.emoji} ${item.name}", maxLines = 1) },
                    )
                }
            }
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(7.dp),
            ) {
                Text("Come leggere le schede", style = MaterialTheme.typography.titleMedium)
                Text(
                    "Le indicazioni distinguono evidenza media e pratica tradizionale. I risultati possono cambiare con clima, densità e gestione; le associazioni con evidenza limitata non diventano vincoli automatici.",
                )
            }
        }

        if (rules.isEmpty()) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Text(
                    "Nella banca dati iniziale non ci sono ancora consociazioni specifiche sufficientemente documentate per ${selected.name}.",
                    modifier = Modifier.padding(16.dp),
                )
            }
        } else {
            rules.forEach { rule ->
                val otherId = if (rule.firstCatalogId == selected.id) rule.secondCatalogId else rule.firstCatalogId
                val other = CROP_CATALOG.firstOrNull { it.id == otherId }
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text(
                            "${other?.emoji ?: "🌱"} ${other?.name ?: otherId}",
                            style = MaterialTheme.typography.titleLarge,
                        )
                        Text("${rule.relation} · ${rule.evidence}", color = MaterialTheme.colorScheme.primary)
                        Text("Possibile utilità: ${rule.benefit}")
                        Text("Disposizione: ${rule.placement}")
                        Text(
                            "Distanza: almeno ${rule.minimumDistanceCm.toInt()} cm" +
                                (rule.maximumDistanceCm?.let { ", preferibilmente entro ${it.toInt()} cm" } ?: ""),
                            style = MaterialTheme.typography.bodySmall,
                        )
                        rule.sourceIds.forEach sourceLoop@ { sourceId ->
                            val source = companionSource(sourceId) ?: return@sourceLoop
                            TextButton(
                                onClick = {
                                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(source.url)))
                                },
                            ) {
                                Text("Fonte: ${source.title}")
                            }
                        }
                    }
                }
            }
        }
    }
}
