package com.hehematch.android.hehematchandrodapp.ui.finder.saveuser.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hehematch.android.hehematchandrodapp.core.shared.UiState
import com.hehematch.android.hehematchandrodapp.ui.finder.core.model.FindUserModel
import com.hehematch.android.hehematchandrodapp.ui.finder.core.repository.ResultsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SaveUserViewModel @Inject constructor(
    private val userRepository: ResultsRepository
) : ViewModel() {
    private val _saveData = MutableStateFlow<UiState<String>>(UiState.Loading)
    val saveData: StateFlow<UiState<String>> = _saveData

    init {
        val data = listOf(FindUserModel())
        saveDataList(data)
    }
    private fun saveDataList(dataList: List<FindUserModel>) {
        _saveData.value = UiState.Loading
        viewModelScope.launch {
            try {
                userRepository.saveDataList(dataList).collect { result ->
                    _saveData.value = result
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _saveData.value = UiState.Error(e.toString())
            }
        }
    }
}


