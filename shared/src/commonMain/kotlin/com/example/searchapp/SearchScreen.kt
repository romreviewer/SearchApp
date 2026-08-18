package com.example.searchapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.searchapp.data.SearchUiState
import com.example.searchapp.domain.model.GithubRepository

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel,
    onRepoSelected: (GithubRepository) -> Unit
) {
    val state by viewModel.state.collectAsState()
    val listState = rememberLazyListState()/*LaunchedEffect(listState,state.repos.size){
        snapshotFlow { listState.layoutInfo.vi }
    }*/
    Scaffold(
        modifier = modifier, topBar = {
            TopAppBar(
                title = { Text("Github Search Repo") },
            )
        }) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            OutlinedTextField(
                label = {
                    Text("Search Repository")
                },
                modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp, horizontal = 16.dp),
                value = state.query,
                singleLine = true,
                onValueChange = viewModel::onQueryChange
            )
            when {
                state.isInitialLoading -> SkeletonList()
                else -> RepoList(state, listState, onRepoSelected)
            }
        }
    }
}

@Composable
private fun RepoList(
    state: SearchUiState, listState: LazyListState, onRepoSelected: (GithubRepository) -> Unit
) {
    LazyColumn(
        state = listState,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)
    ) {
        items(state.repos) {
            Card(
                Modifier.fillMaxWidth().clickable {
                    onRepoSelected(it)
                },
                border = BorderStroke(
                    1.dp,
                    color = Color.Black
                )
            ) {
                Column(modifier = Modifier.padding(5.dp)) {
                    Text(text = it.fullName)
                    it.description?.let { des ->
                        Text(text = des)
                    }
                    Row {
                        Text("* ${it.stars}")
                        Spacer(Modifier.width(5.dp))
                        Text("${it.language}")
                    }
                }
            }
        }
    }
}

@Composable
fun SkeletonList(modifier: Modifier = Modifier) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        repeat(5) {
            Card {
                Column {
                    Box(
                        Modifier.fillMaxWidth(fraction = 0.55f).height(18.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    )
                    Spacer(Modifier.height(10.dp))
                    Box(
                        Modifier.fillMaxWidth().height(18.dp).height(14.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    )
                    Spacer(Modifier.height(8.dp))
                    Box(
                        Modifier.fillMaxWidth(0.35f).height(14.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun SearchPreview() {

}

@Preview
@Composable
private fun SkeletonListPreview() {
    SkeletonList()
}
