package com.hehematch.android.hehematchandrodapp.ui.finder.resultsuser.adapter

import android.view.View
import com.hehematch.android.hehematchandrodapp.core.shared.base.BaseListAdapter
import com.hehematch.android.hehematchandrodapp.databinding.ItemResultsUserBinding
import com.hehematch.android.hehematchandrodapp.ui.finder.core.model.FindUserModel

class ResultsUserAdapter(
    private val onItemClick: (FindUserModel) -> Unit
) : BaseListAdapter<FindUserModel, ItemResultsUserBinding>(ItemResultsUserBinding::inflate) {
    override fun onItemBind(): (FindUserModel, ItemResultsUserBinding, View, Int) -> Unit =
        { item, binding, itemView, adapterPosition ->
            binding.apply {
                tvName.text = item.username
            }
            itemView.setOnClickListener {
                onItemClick(item)
            }
        }
}