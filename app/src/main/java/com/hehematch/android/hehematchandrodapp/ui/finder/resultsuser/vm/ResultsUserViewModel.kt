package com.hehematch.android.hehematchandrodapp.ui.finder.resultsuser.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hehematch.android.hehematchandrodapp.core.shared.UiState
import com.hehematch.android.hehematchandrodapp.ui.finder.core.model.FindUserModel
import com.hehematch.android.hehematchandrodapp.ui.finder.core.repository.ResultsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResultsUserViewModel @Inject constructor(
    private val userRepository: ResultsRepository
) : ViewModel() {
    private val _allData = MutableStateFlow<UiState<List<FindUserModel>>>(UiState.Loading)
    val allData: StateFlow<UiState<List<FindUserModel>>> = _allData

    init {
        loadUsers()
    }

    private fun loadUsers() {
        viewModelScope.launch {
            _allData.value = UiState.Loading
            userRepository.loadUsers()
                .catch { e ->
                    _allData.value = UiState.Error(e.toString())
                }
                .collect { uiState ->
                    _allData.value = uiState
                }
        }
    }

}