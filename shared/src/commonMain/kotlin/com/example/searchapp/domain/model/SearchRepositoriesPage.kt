package com.example.searchapp.domain.model

data class SearchRepositoriesPage(
    val repositories: List<GithubRepository>,
    val totalCount: Int,
    val incompleteResults: Boolean,
    val page: Int,
    val pageSize: Int,
)
