package com.example.searchapp.DetailScreen

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.searchapp.SkeletonList
import com.example.searchapp.domain.model.GithubRepositoryDetails

@Composable
fun DetailScreen(viewModel: DetailViewmodel, onBack: () -> Unit) {
    val state by viewModel.state.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Repository details") },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("Back") }
                },
            )
        },
    ) { paddingValues ->
        when {
            state.isLoading -> SkeletonList(Modifier.padding(paddingValues).padding(16.dp))
            state.details != null -> DetailScreenContent(
                detail = state.details!!,
                modifier = Modifier.padding(paddingValues),
            )
            else -> DetailErrorContent(
                message = state.error ?: "Repository details are unavailable.",
                onRetry = viewModel::load,
                modifier = Modifier.padding(paddingValues),
            )
        }
    }
}

@Composable
fun DetailScreenContent(detail: GithubRepositoryDetails, modifier: Modifier = Modifier) {
    val uriHandler = LocalUriHandler.current
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(detail.fullName, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text(
                        detail.description ?: "No description provided.",
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Text("Owner · ${detail.ownerLogin}", style = MaterialTheme.typography.labelLarge)
                }
            }
        }

        item { SectionTitle("Repository activity") }
        item {
            MetricGrid(
                listOf(
                    "Stars" to detail.stars.toString(),
                    "Forks" to detail.forks.toString(),
                    "Watchers" to detail.watchers.toString(),
                    "Open issues" to detail.openIssues.toString(),
                    "Network" to detail.networkCount.toString(),
                    "Subscribers" to detail.subscribers.toString(),
                ),
            )
        }

        item { SectionTitle("Repository information") }
        items(
            listOf(
                "Language" to (detail.language ?: "Not specified"),
                "Default branch" to detail.defaultBranch,
                "Size" to "${detail.sizeInKilobytes} KB",
                "Created" to detail.createdAt,
                "Last updated" to detail.updatedAt,
                "Last pushed" to (detail.pushedAt ?: "No push date available"),
                "Private" to detail.isPrivate.toDisplayValue(),
                "Fork" to detail.isFork.toDisplayValue(),
                "Archived" to detail.isArchived.toDisplayValue(),
                "Disabled" to detail.isDisabled.toDisplayValue(),
                "Repository ID" to detail.id.toString(),
            ),
        ) { (label, value) -> DetailRow(label, value) }

        item { SectionTitle("Links") }
        item {
            DetailRow(
                label = "GitHub repository",
                value = detail.url,
                isLink = true,
                onClick = { uriHandler.openUri(detail.url) },
            )
        }
        detail.homepage?.takeIf { it.isNotBlank() }?.let { homepage ->
            item {
                DetailRow(
                    label = "Homepage",
                    value = homepage,
                    isLink = true,
                    onClick = { uriHandler.openUri(homepage) },
                )
            }
        }
    }
}

@Composable
private fun MetricGrid(metrics: List<Pair<String, String>>) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        metrics.chunked(2).forEach { rowMetrics ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                rowMetrics.forEach { (label, value) ->
                    Card(modifier = Modifier.weight(1f)) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            Text(label, style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }
                if (rowMetrics.size == 1) Spacer(Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(text, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
}

@Composable
private fun DetailRow(
    label: String,
    value: String,
    isLink: Boolean = false,
    onClick: (() -> Unit)? = null,
) {
    Card(
        modifier = Modifier.fillMaxWidth().then(
            if (isLink && onClick != null) Modifier.clickable(onClick = onClick) else Modifier,
        ),
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
            Text(label, style = MaterialTheme.typography.labelMedium)
            Text(
                value,
                style = MaterialTheme.typography.bodyLarge,
                color = if (isLink) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                maxLines = if (isLink) 2 else Int.MAX_VALUE,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun DetailErrorContent(message: String, onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text("Could not load repository details", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))
        Text(message, modifier = Modifier.padding(top = 8.dp), style = MaterialTheme.typography.bodyMedium)
        Button(onClick = onRetry, modifier = Modifier.padding(top = 16.dp)) { Text("Retry") }
    }
}

private fun Boolean.toDisplayValue(): String = if (this) "Yes" else "No"
