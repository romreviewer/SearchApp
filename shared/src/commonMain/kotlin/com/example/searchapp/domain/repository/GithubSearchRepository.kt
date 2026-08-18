package com.example.searchapp.domain.repository

import com.example.searchapp.domain.model.GithubRepositoryDetails
import com.example.searchapp.domain.model.SearchRepositoriesPage

interface GithubSearchRepository {
    suspend fun getRepository(
        fullName: String
    ): GithubRepositoryDetails

    suspend fun searchRepositories(
        query: String,
        page: Int,
        pageSize: Int,
    ): SearchRepositoriesPage
}
