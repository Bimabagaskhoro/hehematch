package com.hehematch.android.hehematchandrodapp.core.shared

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseListAdapter<T : Any, VB : ViewBinding>(
    private val inflaterFactory: (LayoutInflater, ViewGroup?, Boolean) -> VB
) : ListAdapter<T, BaseListAdapter.BaseViewHolder<T, VB>>(BaseItemCallback<T>()) {

    abstract fun onItemBind(): (T, VB, View, Int) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<T, VB> {
        val binding = inflaterFactory(LayoutInflater.from(parent.context), parent, false)
        val view = binding.root
        return BaseViewHolder(view, binding, onItemBind())
    }

    override fun onBindViewHolder(holder: BaseViewHolder<T, VB>, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class BaseViewHolder<T : Any, VB : ViewBinding>(
        view: View,
        private val binding: VB,
        private val onItemBind: (T, VB, View, Int) -> Unit
    ) : RecyclerView.ViewHolder(view) {
        fun bind(item: T) {
            onItemBind(item, binding, itemView, adapterPosition)
        }
    }

    class BaseItemCallback<T : Any> : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
            return oldItem.toString() == newItem.toString()
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
            return oldItem == newItem
        }
    }
}