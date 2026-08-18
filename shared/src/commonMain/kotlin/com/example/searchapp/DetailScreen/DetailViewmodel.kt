package com.example.searchapp.DetailScreen

import com.example.searchapp.DetailScreen.model.DetailUiState
import com.example.searchapp.domain.repository.GithubSearchRepository
import com.example.searchapp.domain.usecase.GetGithubRepositoryUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailViewmodel(
    private val repository: GithubSearchRepository,
    private val name: String
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val mutableState = MutableStateFlow(DetailUiState())
    val state: StateFlow<DetailUiState> = mutableState.asStateFlow()

    init {
        load()
    }

    fun load() {
        scope.launch {
            mutableState.value = DetailUiState(isLoading = true)
            runCatching {
                GetGithubRepositoryUseCase(repository = repository)(name)
            }.onSuccess { details ->
                mutableState.value = DetailUiState(isLoading = false, details = details)
            }.onFailure { error ->
                mutableState.value = DetailUiState(
                    isLoading = false,
                    error = error.message ?: "Unable to load repository details.",
                )
            }
        }
    }

    fun clear() {
        scope.cancel()
    }
}
