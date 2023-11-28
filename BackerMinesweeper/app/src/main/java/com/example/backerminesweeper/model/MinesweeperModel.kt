package com.example.backerminesweeper.model
import android.util.Log

object MinesweeperModel {
    var numFlags = 3 // number of flags (aka Prof Ekler)
    private lateinit var makeField: Array<Array<Field>>

    private fun gameArea(size: Int) {
        makeField = Array(size){ Array(size) {Field(0, 0, false, false)} }
        placeMines(3)
        for (i in 0..4) {
            for (j in 0..4) {
                mineCheck(i, j)
            }
        }
    }

    private val gameBoard = gameArea(25)

    fun getFieldContent(x: Int, y: Int) = makeField[x][y]

    fun placeFlag(tX: Int, tY: Int) {
        val cell = makeField[tX][tY]
        if (!cell.wasClicked) {
            if (cell.isFlagged) {
                cell.isFlagged = false
                numFlags++
            } else if (numFlags > 0) {
                cell.isFlagged = true
                numFlags--
            }
        }
    }

    private fun placeMines(mineCount: Int) {
        val random = java.util.Random()
        var mines = 0
        while (mines < mineCount) {
            val ranRow = random.nextInt(5)
            val ranCol = random.nextInt(5)

            if (makeField[ranRow][ranCol].type != 1) {
                makeField[ranRow][ranCol].type = 1
                mines++
            }
        }

    }

    private fun mineCheck(x: Int, y: Int) {
        val result = (-1..1).sumOf { i ->
            (-1..1).count { j ->
                val newX = x + i
                val newY = y + j
                newX in 0 until 5 && newY in 0 until 5 && makeField[newX][newY]?.type == 1
            }
        }

        if (makeField[x][y].type == 0) {
            makeField[x][y].minesNear = result
        }
    }

    fun resetModel() {
        for (i in 0..4) {
            for (j in 0..4) {
                makeField[i][j] = Field(0, 0, false, false)
            }
        }
        placeMines(3)
        numFlags = 3
        for (i in 0..4) {
            for (j in 0..4) {
                mineCheck(i, j)
            }
        }
    }

}