package com.example.searchapp.domain.model

data class GithubRepository(
    val id: Long,
    val name: String,
    val fullName: String,
    val description: String?,
    val url: String,
    val stars: Int,
    val language: String?,
    val ownerLogin: String,
    val ownerAvatarUrl: String,
)
