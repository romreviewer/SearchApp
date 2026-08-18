package com.example.searchapp.domain.usecase

import com.example.searchapp.domain.model.GithubRepositoryDetails
import com.example.searchapp.domain.repository.GithubSearchRepository

class GetGithubRepositoryUseCase(
    private val repository: GithubSearchRepository,
) {
    suspend operator fun invoke(
        fullName: String,
    ): GithubRepositoryDetails {

        return repository.getRepository(
            fullName = fullName
        )
    }
}
