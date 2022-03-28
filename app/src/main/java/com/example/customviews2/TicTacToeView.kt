package com.example.customviews2

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import kotlin.properties.Delegates

class TicTacToeView(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int,
    defStyleRes: Int
    ): View(context, attrs, defStyleAttr, defStyleRes) {

    private var player1Color by Delegates.notNull<Int>()
    private var player2Color by Delegates.notNull<Int>()
    private var gridColor by Delegates.notNull<Int>()

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): this(context, attrs, defStyleAttr, R.style.DefaultTicTacToeFieldStyle)
    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, R.attr.ticTacToeFieldStyle)
    constructor(context: Context): this(context, null)

    init {
        if (attrs!= null){
            initAttributes(attrs, defStyleAttr, defStyleRes)
        } else{
            initDefaultColors()
        }
    }

    private fun initDefaultColors() {
        player1Color = PLAYER1_DEFAULT_COLOR
        player2Color = PLAYER2_DEFAULT_COLOR
        gridColor = GRID_DEFAULT_COLOR
    }

    private fun initAttributes(attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.TicTacToeView, defStyleAttr, defStyleRes)
        player1Color = typedArray.getColor(R.styleable.TicTacToeView_player1Color, PLAYER1_DEFAULT_COLOR)
        player2Color = typedArray.getColor(R.styleable.TicTacToeView_player2Color, PLAYER2_DEFAULT_COLOR)
        gridColor = typedArray.getColor(R.styleable.TicTacToeView_gridColor, GRID_DEFAULT_COLOR)

        typedArray.recycle()
    }

    companion object{
        const val PLAYER1_DEFAULT_COLOR = Color.GREEN
        const val PLAYER2_DEFAULT_COLOR = Color.RED
        const val GRID_DEFAULT_COLOR = Color.GRAY
    }
}