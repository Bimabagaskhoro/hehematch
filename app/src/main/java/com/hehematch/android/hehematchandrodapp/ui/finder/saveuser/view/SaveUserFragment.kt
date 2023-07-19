package com.hehematch.android.hehematchandrodapp.ui.finder.saveuser.view

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.hehematch.android.hehematchandrodapp.core.shared.CustomToast
import com.hehematch.android.hehematchandrodapp.core.shared.UiState
import com.hehematch.android.hehematchandrodapp.core.shared.base.BaseFragment
import com.hehematch.android.hehematchandrodapp.databinding.FragmentSaveUserBinding
import com.hehematch.android.hehematchandrodapp.ui.finder.core.model.FindUserModel
import com.hehematch.android.hehematchandrodapp.ui.finder.saveuser.vm.SaveUserViewModel
import kotlinx.coroutines.launch

class SaveUserFragment :
    BaseFragment<FragmentSaveUserBinding, SaveUserViewModel>(FragmentSaveUserBinding::inflate) {
    override val viewModel: SaveUserViewModel by viewModels()

    override fun initView() {
        binding.tvAddUser.apply {
            setOnClickListener {
                observeData()
            }
            text = "save user"
        }
    }


    override fun observeData() {
        val data = listOf(
            FindUserModel(
                "user 1",
                0.0,
                0.0
            ),
            FindUserModel(
                "user 2",
                0.0,
                0.0
            ),
            FindUserModel(
                "user 3",
                0.0,
                0.0
            )
        )
        lifecycleScope.launch {
            viewModel.saveData.collect { uiState ->
                when (uiState) {
                    is UiState.Loading -> {

                    }
                    is UiState.Success -> {
                        CustomToast.showSuccess(requireContext())
                    }
                    is UiState.Error -> {
                        if (uiState.message == "Connection") {
                            CustomToast.showNoInternet(requireContext())
                        } else {
                            CustomToast.showError(requireContext())
                        }
                    }
                }
            }
        }
    }
}