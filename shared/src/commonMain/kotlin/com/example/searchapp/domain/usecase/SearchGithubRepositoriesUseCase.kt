package com.example.searchapp.domain.usecase

import com.example.searchapp.domain.model.SearchRepositoriesPage
import com.example.searchapp.domain.repository.GithubSearchRepository

class SearchGithubRepositoriesUseCase(
    private val repository: GithubSearchRepository,
) {
    suspend operator fun invoke(
        query: String,
        page: Int = FIRST_PAGE,
        pageSize: Int = DEFAULT_PAGE_SIZE,
    ): SearchRepositoriesPage {
        require(query.isNotBlank()) { "Search query must not be blank." }
        require(page >= FIRST_PAGE) { "Page must be at least $FIRST_PAGE." }
        require(pageSize in 1..MAX_PAGE_SIZE) { "Page size must be between 1 and $MAX_PAGE_SIZE." }

        return repository.searchRepositories(
            query = query.trim(),
            page = page,
            pageSize = pageSize,
        )
    }

    private companion object {
        const val FIRST_PAGE = 1
        const val DEFAULT_PAGE_SIZE = 10
        const val MAX_PAGE_SIZE = 100
    }
}
