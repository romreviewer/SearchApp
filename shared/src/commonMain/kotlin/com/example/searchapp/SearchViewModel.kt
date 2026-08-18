package com.example.searchapp

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.annotation.FrequentlyChangingValue
import androidx.lifecycle.ViewModel
import com.example.searchapp.data.SearchUiState
import com.example.searchapp.domain.repository.GithubSearchRepository
import com.example.searchapp.domain.usecase.SearchGithubRepositoriesUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class SearchViewModel(private val repository: GithubSearchRepository) {
    private val mutableState = MutableStateFlow(SearchUiState())
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    val state: StateFlow<SearchUiState> = mutableState.asStateFlow()
    private var searchJob: Job? = null
    private var currentPage = 0

    fun onQueryChange(value: String) {
        searchJob?.cancel()
        val query = value.trim()
        currentPage = 0
        mutableState.value = SearchUiState(query = value)
        if (query.isBlank()) return
        searchJob = scope.launch {
            delay(400.milliseconds)
            loadFirstPage(query)
        }
    }

    private suspend fun loadFirstPage(query: String) {
        mutableState.value = SearchUiState(
            query = mutableState.value.query,
            isInitialLoading = true,
            hasSearched = true
        )
        loadPage(query, 1, append = false)
    }

    private suspend fun loadPage(query: String, page: Int, append: Boolean) {
        val before = mutableState.value
        mutableState.value = if (append) before.copy(
            isLoadingMore = true, error = null
        ) else {
            before.copy(
                isInitialLoading = true, error = null
            )
        }
        runCatching {
            val useCase = SearchGithubRepositoriesUseCase(repository)
            val result = useCase(query = query, page = page)
            currentPage = page
            val all = if (append) before.repos + result.repositories else result.repositories
            mutableState.value = mutableState.value.copy(
                repos = all.distinctBy { it.fullName },
                isInitialLoading = false,
                isLoadingMore = false,
                canLoadMore = result.repositories.isNotEmpty() && page * 10 < result.totalCount,
                error = null
            )
        }.getOrElse { error ->
            mutableState.value = mutableState.value.copy(
                isInitialLoading = false,
                isLoadingMore = false,
                error = error.message ?: "Something went wrong"
            )
        }
    }
    fun clear(){

    }
}