package com.example.searchapp.data

import com.example.searchapp.data.remote.GithubRepositoryDto
import com.example.searchapp.data.remote.GithubRepositoryDetailsDto
import com.example.searchapp.data.remote.GithubSearchResponseDto
import com.example.searchapp.domain.model.GithubRepository
import com.example.searchapp.domain.model.GithubRepositoryDetails
import com.example.searchapp.domain.model.SearchRepositoriesPage
import com.example.searchapp.domain.repository.GithubSearchRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.HttpHeaders

class GithubSearchRepositoryImpl(
    private val httpClient: HttpClient,
) : GithubSearchRepository {

    override suspend fun getRepository(
        fullName: String
    ): GithubRepositoryDetails {
        return httpClient.get("https://api.github.com/repos/$fullName") {
            headers.append(HttpHeaders.Accept, "application/vnd.github+json")
        }.body<GithubRepositoryDetailsDto>().toDomain()
    }

    override suspend fun searchRepositories(
        query: String,
        page: Int,
        pageSize: Int,
    ): SearchRepositoriesPage {
        val response = httpClient.get("https://api.github.com/search/repositories") {
            headers.append(HttpHeaders.Accept, "application/vnd.github+json")
            parameter("q", query)
            parameter("page", page)
            parameter("per_page", pageSize)
        }.body<GithubSearchResponseDto>()

        return SearchRepositoriesPage(
            repositories = response.items.map { it.toDomain() },
            totalCount = response.total_count,
            incompleteResults = response.incomplete_results,
            page = page,
            pageSize = pageSize,
        )
    }

    private fun GithubRepositoryDto.toDomain() = GithubRepository(
        id = id,
        name = name,
        fullName = full_name,
        description = description,
        url = html_url,
        stars = stargazers_count,
        language = language,
        ownerLogin = owner.login,
        ownerAvatarUrl = owner.avatar_url,
    )

    private fun GithubRepositoryDetailsDto.toDomain() = GithubRepositoryDetails(
        id = id,
        name = name,
        fullName = full_name,
        description = description,
        url = html_url,
        isPrivate = isPrivate,
        isFork = fork,
        isArchived = archived,
        isDisabled = disabled,
        homepage = homepage,
        sizeInKilobytes = size,
        stars = stargazers_count,
        watchers = watchers_count,
        forks = forks_count,
        openIssues = open_issues_count,
        networkCount = network_count,
        subscribers = subscribers_count,
        language = language,
        defaultBranch = default_branch,
        createdAt = created_at,
        updatedAt = updated_at,
        pushedAt = pushed_at,
        ownerLogin = owner.login,
        ownerAvatarUrl = owner.avatar_url,
    )
}
