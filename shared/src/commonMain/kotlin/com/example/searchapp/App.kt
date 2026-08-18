package com.example.searchapp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.searchapp.DetailScreen.DetailScreen
import com.example.searchapp.DetailScreen.DetailViewmodel
import com.example.searchapp.data.GithubSearchRepositoryImpl
import com.example.searchapp.data.remote.createGithubHttpClient
import com.example.searchapp.domain.model.GithubRepository
import com.example.searchapp.domain.repository.GithubSearchRepository
import org.jetbrains.compose.resources.painterResource

import searchapp.shared.generated.resources.Res
import searchapp.shared.generated.resources.compose_multiplatform

@Composable
@Preview
fun App() {
    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        val repository = remember {
            GithubSearchRepositoryImpl(createHttpClient())
        }
        val viewModel = remember { SearchViewModel(repository) }
        var selected by remember { mutableStateOf<GithubRepository?>(null) }
        DisposableEffect(viewModel) {
            onDispose { viewModel.clear() }
        }
        if (selected == null) {
            SearchScreen(
                viewModel = viewModel,
                onRepoSelected = { selected = it },

                )
        } else if (selected != null) {
            val detailViewmodel = remember(selected!!.fullName) {
                DetailViewmodel(repository, selected!!.fullName)
            }
            DetailScreen(
                detailViewmodel,
                onBack = { selected = null }
            )
        }
    }
}