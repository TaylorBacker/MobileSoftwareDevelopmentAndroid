package com.example.backerminesweeper.view
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.util.Log
import android.view.MotionEvent
import com.example.backerminesweeper.MainActivity
import com.example.backerminesweeper.R
import com.example.backerminesweeper.model.MinesweeperModel
import com.example.backerminesweeper.model.MinesweeperModel.getFieldContent
import com.example.backerminesweeper.model.MinesweeperModel.numFlags

class MinesweeperView (context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    // for painting the background
    private val paintBackground = Paint()
    private val paintLine = Paint()
    // images variables
    private var flagImg = BitmapFactory.decodeResource(resources, R.drawable.flag)
    private var topImg = BitmapFactory.decodeResource(resources, R.drawable.empty)
    private var oneImg = BitmapFactory.decodeResource(resources, R.drawable.one)
    private var twoImg = BitmapFactory.decodeResource(resources, R.drawable.two)
    private var threeImg = BitmapFactory.decodeResource(resources, R.drawable.three)
    private var mineImg = BitmapFactory.decodeResource(resources, R.drawable.mine)
    private var blankImg = BitmapFactory.decodeResource(resources, R.drawable.blankcell)

    init {
        paintBackground.color = Color.BLACK
        paintBackground.style = Paint.Style.FILL

        paintLine.color = Color.WHITE
        paintLine.style = Paint.Style.STROKE
        paintLine.strokeWidth = 10f
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        blankImg = Bitmap.createScaledBitmap(blankImg,w/5,h/5,false)
        topImg = Bitmap.createScaledBitmap(topImg, w / 5, h / 5, false)
        flagImg = Bitmap.createScaledBitmap(flagImg, w / 5, h / 5, false)
        mineImg = Bitmap.createScaledBitmap(mineImg, w / 5, h / 5, false)
        oneImg = Bitmap.createScaledBitmap(oneImg, w / 5, h / 5, false)
        twoImg = Bitmap.createScaledBitmap(twoImg, w / 5, h / 5, false)
        threeImg = Bitmap.createScaledBitmap(threeImg, w / 5, h / 5, false)
    }

    override fun onDraw(canvas: Canvas?) {
        // draws board & players
        super.onDraw(canvas)
        canvas?.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paintBackground)
        drawGameArea(canvas)
        drawPlayers(canvas)
    }

    private fun drawGameArea(canvas: Canvas?) {
        //draws outline of gameboard and applies the top Image on each square
        canvas?.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paintLine)
        for (x in 1..4) {
            canvas?.drawLine(
                0f, (x * height / 5).toFloat(), width.toFloat(), (x * height / 5).toFloat(),
                paintLine
            )
            canvas?.drawLine(
                (x * width / 5).toFloat(), 0f, (x * width / 5).toFloat(), height.toFloat(),
                paintLine
            )
        }
        for (x in 0..5) {
            for (y in 0..5) {
                canvas?.drawBitmap(topImg, x * width/ 5.toFloat(), y * height/ 5.toFloat(), paintLine)
            }
        }
    }

    private fun drawPlayers(canvas: Canvas?) {
        // draws the bombs, numbers, and flags on square in fucntion
        for (i in 0..4) {
            for (j in 0..4) {
                var whichSquare = getFieldContent(i, j)
                if (whichSquare.type == 0 && whichSquare.wasClicked && !whichSquare.isFlagged) {
                    drawNumber(canvas, i, j) // draws appropirate number on sqaure
                    Log.e("activity", "empty clicked, adjacent: ${whichSquare.minesNear}")
                }
                else if (getFieldContent(i, j).isFlagged) {
                    canvas?.drawBitmap(flagImg, i* width/5.toFloat(), j*height/5.toFloat(), paintLine)
                }
                else if (whichSquare.type == 1  && whichSquare.wasClicked) {
                    drawBombs(canvas)
                }
            }
        }
    }
    private fun drawBombs(canvas: Canvas?) {
        // handles where/if a square is a mine and draws the mine on
        for (i in 0..4) {
            for (j in 0..4) {
                var whichSquare = getFieldContent(i, j)
                if (whichSquare.type == 1){
                    canvas?.drawBitmap(mineImg, (i* width)/5.toFloat(), (j*height)/5.toFloat(), paintLine)
                }
            }
        }
    }

    private fun drawNumber(canvas: Canvas?, i: Int, j: Int) {
        // draws mines on square
        // I only chose to do up to the #3 for numbers because with a max of 3 mines, a cell cannot
        // have more than 3 mines touching in
        when(getFieldContent(i, j).minesNear) {
            0 -> canvas?.drawBitmap(blankImg, (i* width)/5.toFloat(), (j*height)/5.toFloat(), paintLine)
            1 -> canvas?.drawBitmap(oneImg, (i* width)/5.toFloat(), (j*height)/5.toFloat(), paintLine)
            2 -> canvas?.drawBitmap(twoImg, (i* width)/5.toFloat(), (j*height)/5.toFloat(), paintLine)
            3 -> canvas?.drawBitmap(threeImg, (i* width)/5.toFloat(), (j*height)/5.toFloat(), paintLine)
        }
    }

    @SuppressLint("StringFormatMatches")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        // if clicked
        if (event.action == MotionEvent.ACTION_DOWN) {
            val context = context as MainActivity
            val tX = (event.x / (width / 5)).toInt()
            val tY = (event.y / (height / 5)).toInt()
            // if you check the flag button
            if (context.isFlagModeOn()) {
                MinesweeperModel.placeFlag(tX, tY)
            } else {
                // get square content, apply the check to see if surroundings are empty
                // check if won or lost
                val cell = getFieldContent(tX, tY)
                cell.wasClicked = true
                emptyFieldCheck(tX, tY)
                checkWin()
                checkLoss(tX, tY)
            }

            val numFlags = context.getString(R.string.flags2, numFlags)
            context.setMessage(numFlags)
            checkWin()
            invalidate()
        }
        return true
    }

    private fun checkLoss(x: Int, y: Int) {
        var current = getFieldContent(x, y)
        // if square is a mine
        if (current.type == 1 && !current.isFlagged) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle(context.getString(R.string.game_over))
                .setMessage(context.getString(R.string.you_hit_a_mine))
                .setPositiveButton(context.getString(R.string.reset)) { dialog, _ ->
                    // Handle button click if needed
                    reset()
                    dialog.dismiss()
                }
            val dialog = builder.create()
            dialog.show()
        }
    }

    private fun checkWin() {
        val won = (0..4).all { i ->
            (0..4).all { j ->
                val current = getFieldContent(i, j)
                current.type != 1 || current.isFlagged
            }
        }

        if (won) {
            AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.congrats))
                .setMessage(context.getString(R.string.you_win))
                .setPositiveButton(context.getString(R.string.reset2)) { dialog, _ ->
                    reset()
                    dialog.dismiss()
                }
                .create()
                .show()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val w = View.MeasureSpec.getSize(widthMeasureSpec)
        val h = View.MeasureSpec.getSize(heightMeasureSpec)
        val d = if (w == 0) h else if (h == 0) w else if (w < h) w else h
        setMeasuredDimension(d, d)
    }

    private fun emptyFieldCheck(i: Int, j: Int) {
        val cell = getFieldContent(i, j)
        if (cell.minesNear > 0) {
            return
        }
        cell.wasClicked = true
        for (x in -1..1) {
            for (y in -1..1) {
                if (5 > (x + i) && (x + i) >= 0 && 5 > (y + j) && (y + j) >= 0) {
                    val neighbor = getFieldContent(x + i, y + j)
                    if (neighbor.minesNear == 0 && !neighbor.wasClicked && neighbor.type != 1) {
                        emptyFieldCheck(x + i, y + j)
                    }
                }
            }
        }
    }
    
    @SuppressLint("StringFormatMatches")
    fun reset() {
        MinesweeperModel.resetModel()
        (context as MainActivity).setMessage(context.getString(R.string.flags, numFlags))
        invalidate()
    }
}