package com.hehematch.android.hehematchandrodapp.uikit

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.hehematch.android.hehematchandrodapp.R
import com.hehematch.android.hehematchandrodapp.databinding.UikitButtonBinding

class UiKitButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private val binding: UikitButtonBinding
    private var actionClickListener: (() -> Unit)? = null
    private var cardColor: Int = ContextCompat.getColor(context, R.color.button_primary)
    private var textColor: Int = ContextCompat.getColor(context, R.color.white)

    init {
        binding = UikitButtonBinding.inflate(LayoutInflater.from(context), this, true)
        binding.parentBtnUiKit.setCardBackgroundColor(cardColor)
        binding.tvBtnUiKit.setTextColor(textColor)

        binding.parentBtnUiKit.setOnClickListener {
            actionClickListener?.invoke()
        }
    }

    fun setTextButton(text: String) {
        binding.tvBtnUiKit.text = text
    }

    fun setClickButton(listener: () -> Unit) {
        actionClickListener = listener
    }
}