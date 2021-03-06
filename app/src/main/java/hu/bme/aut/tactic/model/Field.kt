package hu.bme.aut.tactic.model

import kotlin.math.abs


class Field(var x: Int, var y: Int) {
    private var player: PLAYER? = null
    var sign: SIGN? = null
    private var highlighted: Boolean = false

    constructor(x: Int, y: Int, player: PLAYER?, sign: SIGN?, highlighted: Boolean) : this(x, y) {
        this.player = player
        this.sign = sign
        this.highlighted = highlighted
    }

    constructor(field: Field?):this(field!!.x, field.y, field.getPlayer(), field.sign, field.isHighlighted())

    fun getPlayer() : PLAYER? { return player }
    //fun getSign() : SIGN? { return sign }
    fun isHighlighted() : Boolean { return highlighted }

    @JvmName("setPlayer1")
    fun setPlayer(player: PLAYER) {
        this.player = player
    }

    @JvmName("setSign1")
    fun setSign(sign: SIGN) {
        this.sign = sign
    }

    fun setHighlighted(highlighted: Boolean) {
        this.highlighted = highlighted
    }

    fun samePlace(fso: Field): Boolean {
        return (fso.x == this.x && fso.y == this.y)
    }

    fun increaseSignValue(): Boolean{
        if(sign == SIGN.BASE)
            return false
        if(sign!!.value >= 6)
            return false
        sign = sign?.getNext()
        return true
    }


    fun attackFrom(from: Field): Boolean{
        if(this.player == from.player)
            return false
            if(!this.isNeighbour(from))
                return false
            when {
                this.sign == SIGN.BASE -> {
                    return if (from.sign!!.value >= 2) {
                        Game.attackAddScore(50)
                        GameHelper.game.gameOver(from.getPlayer())
                        true
                    } else
                        false
                }
                from.sign!!.minus(this.sign!!) >= 1 -> {
                    Game.attackAddScore(from.sign!!.minus(this.sign!!))
                    this.setPlayer(from.getPlayer()!!)
                    this.setSign(from.sign!!.getMinus(this.sign!!))
                    return true
                }
                from.sign!!.minus(this.sign!!)== 0 -> {
                    this.setSign(SIGN.ONE)
                    Game.getInstance().getMap()[from.x][from.y].setSign(SIGN.ONE)
                    return true
                }
            }

        return false
    }


    private fun isNeighbour(field: Field) : Boolean{
        if(this.x == field.x)
            if(abs(this.y - field.y) <= 1)
                return true
        if(this.y == field.y)
            if(abs(this.x - field.x) <= 1)
                return true
        return false
    }
}