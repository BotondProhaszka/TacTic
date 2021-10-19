package hu.bme.aut.tactic.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import hu.bme.aut.tactic.R
import hu.bme.aut.tactic.model.FieldStateObject
import hu.bme.aut.tactic.model.Game
import kotlin.math.ceil

val MIN_CELL_SIZE = 80f

enum class PLAYER {
    RED, BLUE
}

enum class SIGN {
    BASE, ONE, TWO, THREE, FOUR
}

class MapView (context: Context?, attrs: AttributeSet?) : View(context, attrs){

    private val paint = Paint()
    private var mapWidth = 0
    private var mapHeight = 0
    private var cellWidth = 0F
    private var cellHeight = 0F
    private var correct = 9F

    private var game = Game.getInstance()

    private var cells = ArrayList<ArrayList<FieldStateObject?>>()

    private var canvas: Canvas? = null

    init {
        val game = Game.getInstance()
        mapWidth = game.getMapWidth()
        mapHeight = game.getMapHeight()


        paint.style = Paint.Style.FILL
        paint.strokeWidth = 6F
        R.color.asphalt.also { paint.color = it }

        for(i in 0.. mapWidth) {
            var row = ArrayList<FieldStateObject?>()
            for (j in 0..mapHeight) {
                var b = null
                row.add(b)
            }
            cells.add(row)
        }

    }

    override fun onDraw(canvas: Canvas){
        super.onDraw(canvas)
        this.canvas = canvas


        if(cellWidth == 0F) {
            cellWidth = (width - correct) / mapWidth.toFloat()
            if(cellWidth < MIN_CELL_SIZE) {
                cellWidth = MIN_CELL_SIZE

            }
        }
        if(cellHeight == 0F) {
            cellHeight = (height - correct) / (mapHeight.toFloat())
            if(cellHeight < MIN_CELL_SIZE)
                cellHeight = MIN_CELL_SIZE
        }


        R.color.asphalt.also { paint.color = it }
        for(i in 0..mapWidth+1) //fuggoleges
            canvas.drawLine((i * cellWidth) + (correct/2), (correct/2), (i*cellWidth) + (correct/2), height.toFloat() - (correct/2), paint)
        for(i in 0..mapHeight+1) //vizszintes
            canvas.drawLine((correct/2), (i* cellHeight) + (correct/2), width.toFloat() - (correct/2), (i * cellHeight) + (correct/2), paint)

        for(i in 0.. mapWidth) {
            for (j in 0..mapHeight){
                if(cells[i][j] != null) {
                    drawInPos(i, j, cells[i][j])
                    Log.d("Bugfix", "${i}:${j}")

                }
            }
        }
    }




    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        super.onTouchEvent(event)
        event?: return false

        when(event.action){
            MotionEvent.ACTION_DOWN -> {

            }
            MotionEvent.ACTION_MOVE -> {

            }
            MotionEvent.ACTION_UP -> {
                val xTemp = event.x / cellWidth
                val yTemp = event.y / cellHeight
                val x = ceil(xTemp.toDouble()).toInt()
                val y = ceil(yTemp.toDouble()).toInt()
                if(x <= mapWidth && y <= mapHeight && x > 0 && y > 0) {
                    var cellObject = game.clickedOn(x,y)
                    cells[x][y] = cellObject
                    Log.d("Bugfix", "UP: ${x}:${y}")
                    drawInPos(x, y, cellObject)
                    this.invalidate()
                }
            }
        }

        return true
    }




    private fun drawInPos(x: Int, y: Int, fso: FieldStateObject?){
        R.color.asphalt.also { paint.color = it }
        when(fso?.player){
            PLAYER.RED -> R.color.player_red.also { paint.color = it }
            PLAYER.BLUE -> R.color.player_blue.also { paint.color = it }
        }
        var sign = "A"
        when(fso?.sign){
            SIGN.ONE -> sign = "1"
            SIGN.TWO -> sign = "2"
            SIGN.THREE -> sign = "3"
            SIGN.FOUR -> sign = "4"
            SIGN.BASE -> {
                sign = when(fso?.player){
                    PLAYER.RED -> "X"
                    PLAYER.BLUE -> "O"
                }
            }

        }

        Log.d("Bugfix", "drawInPos")
        paint.textSize = 100F
        canvas?.drawText(sign, (x-0.85F) * cellWidth + correct,  (y- 0.3f)*cellHeight, paint)
        //canvas?.drawRect((x-1) * cellWidth + correct,  (y-1) * cellHeight + correct, x*cellWidth,y*cellHeight, paint )
    }

}