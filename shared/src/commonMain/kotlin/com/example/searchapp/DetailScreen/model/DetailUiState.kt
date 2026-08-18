package com.example.searchapp.DetailScreen.model

import com.example.searchapp.domain.model.GithubRepositoryDetails

data class DetailUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val details: GithubRepositoryDetails? = null
)
