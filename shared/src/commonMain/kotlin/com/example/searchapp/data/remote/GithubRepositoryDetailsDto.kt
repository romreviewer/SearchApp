package com.example.searchapp.data.remote

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
internal data class GithubRepositoryDetailsDto(
    val id: Long,
    val name: String,
    val full_name: String,
    val description: String? = null,
    val html_url: String,
    @SerialName("private") val isPrivate: Boolean,
    val fork: Boolean,
    val archived: Boolean,
    val disabled: Boolean,
    val homepage: String? = null,
    val size: Int,
    val stargazers_count: Int,
    val watchers_count: Int,
    val forks_count: Int,
    val open_issues_count: Int,
    val network_count: Int,
    val subscribers_count: Int,
    val language: String? = null,
    val default_branch: String,
    val created_at: String,
    val updated_at: String,
    val pushed_at: String? = null,
    val owner: GithubOwnerDto,
)
