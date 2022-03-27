package com.example.customviews2

import android.content.Context
import android.util.AttributeSet
import android.view.View

class TicTacToeView(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int,
    defStyleRes: Int
    ): View(context, attrs, defStyleAttr, defStyleRes) {

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): this(context, attrs, defStyleAttr, R.style.DefaultTicTacToeFieldStyle)
    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, R.attr.ticTacToeFieldStyle)
    constructor(context: Context): this(context, null)

    init {
        if (attrs!= null){
            initAttributes(attrs, defStyleAttr, defStyleRes)
        }
    }

    private fun initAttributes(attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) {
        TODO("Not yet implemented")
    }
}