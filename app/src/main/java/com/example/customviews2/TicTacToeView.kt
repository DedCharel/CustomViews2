package com.example.customviews2

import android.content.Context
import android.graphics.Color
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import kotlin.math.max
import kotlin.math.min
import kotlin.properties.Delegates

class TicTacToeView(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int,
    defStyleRes: Int
    ): View(context, attrs, defStyleAttr, defStyleRes) {

    var ticTacToeField: TicTacToeField? = null
        set(value){
            field?.listeners?.remove(listener) //при создании удалим старых слушателей
            field = value
            field?.listeners?.add(listener) // добавим новых слушателей
            updateViewSize()
            requestLayout() // вызываетлся для изменения размеров
            invalidate() // вызывается для перерисовки
        }

    private val listener: OnFieldChangedListener ={

    }

    private var player1Color by Delegates.notNull<Int>()
    private var player2Color by Delegates.notNull<Int>()
    private var gridColor by Delegates.notNull<Int>()

    private val fieldRect = RectF(0f, 0f, 0f, 0f)
    private var cellSize: Float = 0f
    private var cellPadding: Float = 0f

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

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        ticTacToeField?.listeners?.add(listener)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        ticTacToeField?.listeners?.remove(listener)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minWidth = suggestedMinimumWidth + paddingLeft + paddingRight
        val minHeight = suggestedMinimumHeight +paddingTop + paddingBottom

        val desiredCellSizeInPixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DESIRED_SELL_SIZE,
            resources.displayMetrics).toInt() // переводим из dp  в пикселы желаемый размер ячейки

        val row = ticTacToeField?.rows ?: 0
        val column = ticTacToeField?.columns ?: 0

        val desiredWidth = max(minWidth, desiredCellSizeInPixels * column + paddingLeft + paddingRight)
        val desiredHeight = max(minHeight, desiredCellSizeInPixels * row + paddingTop + paddingBottom)

        setMeasuredDimension(
            resolveSize(desiredWidth, widthMeasureSpec),
            resolveSize(desiredHeight, heightMeasureSpec)
        )
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        updateViewSize()
    }

    private fun updateViewSize() {
        val field = this.ticTacToeField ?: return

        val safeWidth = width - paddingLeft - paddingRight
        val safeHeight = height - paddingTop - paddingBottom

        val cellWidth = safeWidth / field.columns.toFloat()
        val cellHeight = safeHeight / field.rows.toFloat()

        cellSize = min(cellWidth, cellHeight)
        cellPadding = cellSize * 0.2f

        val fieldWidth = cellSize * field.columns
        val fieldHeight = cellSize * field.rows

        fieldRect.left = paddingLeft + (safeWidth - fieldWidth) / 2  //safeWidth - fieldWidth для центрирования
        fieldRect.top = paddingTop + (safeHeight - fieldHeight) / 2
        fieldRect.right = fieldRect.left + fieldWidth
        fieldRect.bottom = fieldRect.top + fieldHeight
    }

    companion object{
        const val PLAYER1_DEFAULT_COLOR = Color.GREEN
        const val PLAYER2_DEFAULT_COLOR = Color.RED
        const val GRID_DEFAULT_COLOR = Color.GRAY

        const val DESIRED_SELL_SIZE = 50f
    }
}