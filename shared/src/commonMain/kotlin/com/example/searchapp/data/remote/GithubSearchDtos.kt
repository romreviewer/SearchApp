package com.example.searchapp.data.remote

import kotlinx.serialization.Serializable

@Serializable
internal data class GithubSearchResponseDto(
    val total_count: Int,
    val incomplete_results: Boolean,
    val items: List<GithubRepositoryDto>,
)

@Serializable
internal data class GithubRepositoryDto(
    val id: Long,
    val name: String,
    val full_name: String,
    val description: String? = null,
    val html_url: String,
    val stargazers_count: Int,
    val language: String? = null,
    val owner: GithubOwnerDto,
)

@Serializable
internal data class GithubOwnerDto(
    val login: String,
    val avatar_url: String,
)
