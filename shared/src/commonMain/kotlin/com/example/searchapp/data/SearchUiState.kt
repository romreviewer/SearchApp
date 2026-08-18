package com.example.searchapp.data

import com.example.searchapp.domain.model.GithubRepository

data class SearchUiState(
    val query: String = "",
    val repos: List<GithubRepository> = emptyList(),
    val isInitialLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val hasSearched: Boolean = false,
    val canLoadMore: Boolean = false,
    val error: String? = null
)
