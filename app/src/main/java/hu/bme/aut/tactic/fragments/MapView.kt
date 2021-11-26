package hu.bme.aut.tactic.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import hu.bme.aut.tactic.interfaces.GameInterface
import hu.bme.aut.tactic.model.*
import kotlin.math.ceil


@SuppressLint("ResourceAsColor")
class MapView (context: Context?, attrs: AttributeSet?) : View(context, attrs){

    private val paint = Paint()
    private var mapWidth = 0
    private var mapHeight = 0
    private var cellWidth = 0F
    private var cellHeight = 0F
    private var correct = 9F

    private var game: GameInterface = GameHelper.game


    private lateinit var canvas: Canvas

    init {

        game.setMapView(this)
        mapWidth = game.getMapWidth()
        mapHeight = game.getMapHeight()

        paint.style = Paint.Style.FILL
        paint.strokeWidth = 6F
        invalidate()
    }

    fun clearMap(){
        for (i in 0..mapWidth + 1) //fuggoleges
            canvas.drawLine(
                (i * cellWidth) + (correct / 2),
                (correct / 2),
                (i * cellWidth) + (correct / 2),
                height.toFloat() - (correct / 2),
                paint
            )
        for (i in 0..mapHeight + 1) //vizszintes
            canvas.drawLine(
                (correct / 2),
                (i * cellHeight) + (correct / 2),
                width.toFloat() - (correct / 2),
                (i * cellHeight) + (correct / 2),
                paint
            )
    }

    @SuppressLint("ResourceAsColor")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        this.canvas = canvas


        if (cellWidth == 0F) {
            cellWidth = (width - correct) / mapWidth.toFloat()
            if (cellWidth < MIN_CELL_SIZE) {
                cellWidth = MIN_CELL_SIZE

            }
        }
        if (cellHeight == 0F) {
            cellHeight = (height - correct) / (mapHeight.toFloat())
            if (cellHeight < MIN_CELL_SIZE)
                cellHeight = MIN_CELL_SIZE
        }

        when(game.getActualPlayer()){
            PLAYER.BLUE -> paint.setARGB(255, 0, 0, 255)
            PLAYER.RED -> paint.setARGB(255, 255, 0, 0)
        }
        for (i in 0..mapWidth + 1) //fuggoleges
            canvas.drawLine(
                (i * cellWidth) + (correct / 2),
                (correct / 2),
                (i * cellWidth) + (correct / 2),
                height.toFloat() - (correct / 2),
                paint
            )
        for (i in 0..mapHeight + 1) //vizszintes
            canvas.drawLine(
                (correct / 2),
                (i * cellHeight) + (correct / 2),
                width.toFloat() - (correct / 2),
                (i * cellHeight) + (correct / 2),
                paint
            )
        //cellak kirajzolasa
        var map = game.getMap()
        for (a in map)
            for (f in a)
                draw(f)
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
                    game.clickedOn(x,y)
                    this.invalidate()
                }
            }
        }
        return true
    }




    @SuppressLint("ResourceAsColor")
    private fun draw(field: Field) {
        val x = field.x
        val y = field.y

        var sign: String
        when (field.sign) {
            SIGN.ONE -> {
                sign = "1"
            }
            SIGN.TWO -> sign = "2"
            SIGN.THREE -> sign = "3"
            SIGN.FOUR -> sign = "4"
            SIGN.FIVE -> sign = "5"
            SIGN.SIX -> sign = "6"
            SIGN.BASE -> {
                sign = when (field.getPlayer()) {
                    PLAYER.RED -> "X"
                    PLAYER.BLUE -> "O"
                    else -> "."
                }
            }
            else -> return

        }

        val textSize =
            if(cellHeight < cellWidth)
                cellHeight
            else
                cellWidth
        textSize*0.9

        when (field.getPlayer()) {
            PLAYER.RED -> {
                val p2 = Paint()
                p2.textSize = textSize
                if (field.isHighlighted())
                    p2.setARGB(255, 0, 255, 0)
                else
                    p2.setARGB(255, 255, 0, 0)
                canvas.drawText(
                    sign,
                    (x - 0.85F) * cellWidth + correct,
                    (y - 0.3f) * cellHeight + textSize*0.15f,
                    p2
                )
            }
            PLAYER.BLUE -> {
                val p2 = Paint()
                p2.textSize = textSize
                if (field.isHighlighted())
                    p2.setARGB(255, 0, 255, 0)
                else
                    p2.setARGB(255, 0, 0, 255)
                canvas.drawText(
                    sign,
                    (x - 0.85F) * cellWidth + correct,
                    (y - 0.3f) * cellHeight + textSize*0.15f,
                    p2
                )
            }
        }
    }
}