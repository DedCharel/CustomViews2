package com.example.customviews2

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
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

    private val fieldRect = RectF()
    private var cellSize: Float = 0f
    private var cellPadding: Float = 0f
    private var cellRect = RectF()

    private lateinit var player1Paint: Paint
    private lateinit var player2Paint: Paint
    private lateinit var gridPaint: Paint

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): this(context, attrs, defStyleAttr, R.style.DefaultTicTacToeFieldStyle)
    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, R.attr.ticTacToeFieldStyle)
    constructor(context: Context): this(context, null)

    init {
        if (attrs!= null){
            initAttributes(attrs, defStyleAttr, defStyleRes)
        } else{
            initDefaultColors()
        }
        initPaints()
        if (isInEditMode) {
            //для тестирования отрисовки создадим касс TicTacToeField (будет создаваться только в режиме отладки)
            ticTacToeField = TicTacToeField(8, 6)
            ticTacToeField?.setCell(1, 1, Cell.PLAYER_1)
            ticTacToeField?.setCell(3, 1, Cell.PLAYER_1)
            ticTacToeField?.setCell(3, 3, Cell.PLAYER_2)

        }
    }

    private fun initPaints() {
        player1Paint = Paint(Paint.ANTI_ALIAS_FLAG)
        player1Paint.color =player1Color
        player1Paint.style = Paint.Style.STROKE
        player1Paint.strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3f, resources.displayMetrics)

        player2Paint = Paint(Paint.ANTI_ALIAS_FLAG)
        player2Paint.color =player2Color
        player2Paint.style = Paint.Style.STROKE
        player2Paint.strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3f, resources.displayMetrics)

        gridPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        gridPaint.color =gridColor
        gridPaint.style = Paint.Style.STROKE
        gridPaint.strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1f, resources.displayMetrics)
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

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (ticTacToeField == null) return
        if (cellSize == 0f) return
        if (fieldRect.width() <= 0 ) return
        if (fieldRect.height() <= 0) return

        drawGrid(canvas)
        drawCells(canvas)
    }

    private fun drawGrid(canvas: Canvas){
        val field =this.ticTacToeField ?: return

        val xStart = fieldRect.left
        val xEnd = fieldRect.right
        for(i in 0..field.rows){
            val y = fieldRect.top + cellSize * i
            canvas.drawLine(xStart, y, xEnd, y, gridPaint)
        }

        val yStart = fieldRect.top
        val yEnd = fieldRect.bottom
        for(i in 0..field.columns){
            val x = fieldRect.left + cellSize * i
            canvas.drawLine(x, yStart, x, yEnd, gridPaint)
        }
    }
    private fun drawCells(canvas: Canvas){
        val field = this.ticTacToeField ?: return
        for (row in 0 until field.rows){
            for (column in 0 until field.columns){
                val cell = field.getCell(row, column)
                if (cell == Cell.PLAYER_1){
                    drawPlayer1(canvas, row, column)
                } else if (cell == Cell.PLAYER_2){
                    drawPlayer2(canvas, row, column)
                }
            }
        }

    }

    private fun drawPlayer2(canvas: Canvas, row: Int, column: Int) {
        val cellRect = getCellRect(row, column)
        canvas.drawCircle(cellRect.centerX() ,cellRect.centerY(), cellRect.width()/2, player2Paint)
    }

    private fun drawPlayer1(canvas: Canvas, row: Int, column: Int) {
        val cellRect = getCellRect(row, column)
        canvas.drawLine(cellRect.left, cellRect.top, cellRect.right, cellRect.bottom, player1Paint)
        canvas.drawLine(cellRect.right, cellRect.top, cellRect.left, cellRect.bottom, player1Paint)
    }

    private fun getCellRect(row: Int, column: Int) : RectF{
        cellRect.left = fieldRect.left + column * cellSize + cellPadding
        cellRect.top = fieldRect.top + row * cellSize + cellPadding
        cellRect.right = cellRect.left + cellSize - cellPadding * 2
        cellRect.bottom = cellRect.top + cellSize - cellPadding * 2
        return  cellRect
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