package com.example.customviews2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.customviews2.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    var isFirstPlayer = true
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).apply { setContentView(root) }

        binding.ticTakToeView.ticTacToeField = TicTacToeField(10, 10)
        binding.ticTakToeView.actionListener = {row, column, field ->
            val cell = field.getCell(row, column)
            if (cell == Cell.EMPTY){
                if (isFirstPlayer){
                    field.setCell(row,column,Cell.PLAYER_1)
                } else{
                    field.setCell(row, column, Cell.PLAYER_2)
                }
                isFirstPlayer = !isFirstPlayer
            }
        }
        binding.randomFieldButton.setOnClickListener {
            binding.ticTakToeView.ticTacToeField = TicTacToeField(
                Random.nextInt(3, 10),
                Random.nextInt(3, 10)
            )
        }
    }
}