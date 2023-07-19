package com.hehematch.android.hehematchandrodapp.ui.finder.saveuser.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.hehematch.android.hehematchandrodapp.core.shared.UiState
import com.hehematch.android.hehematchandrodapp.databinding.FragmentSaveUserBinding
import com.hehematch.android.hehematchandrodapp.ui.finder.core.model.FindUserModel
import com.hehematch.android.hehematchandrodapp.ui.finder.saveuser.vm.SaveUserViewModel
import com.hehematch.android.hehematchandrodapp.utils.CustomToast
import kotlinx.coroutines.launch

class SaveUserFragment : Fragment() {
    private val viewModel: SaveUserViewModel by viewModels()
    private var _binding: FragmentSaveUserBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSaveUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvAddUser.apply {
            setOnClickListener {
                saveUserDumy()
            }
            text = "save user"
        }
    }


    private fun saveUserDumy() {
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