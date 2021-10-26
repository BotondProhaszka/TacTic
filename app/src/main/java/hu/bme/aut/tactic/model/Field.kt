package hu.bme.aut.tactic.model


class Field(var x: Int, var y: Int) {
    private var player: PLAYER? = null
    private var sign: SIGN? = null
    private var highlighted: Boolean = false

    constructor(x: Int, y: Int, player: PLAYER?, sign: SIGN?, highlighted: Boolean) : this(x, y) {
        this.player = player
        this.sign = sign
        this.highlighted = highlighted
    }

    constructor(field: Field?):this(field!!.x, field.y, field.getPlayer(), field.getSign(), field.isHighlighted())

    fun getPlayer() : PLAYER? { return player }
    fun getSign() : SIGN? { return sign }
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

    fun isEmpty() : Boolean {return (player == null)}

    fun attackFrom(from: Field): Boolean{
        if(this.player != from.player){

            when {
                this.sign == SIGN.BASE -> {
                    return if (from.sign!!.value >= 2) {
                        Game.getInstance().playerWins(from.getPlayer()!!)
                        this.setPlayer(from.getPlayer()!!)
                        //return Field(x, y, from.getPlayer(), SIGN.BASE, false)
                        true
                    } else
                        false
                }
                from.sign!!.minus(this.sign!!) >= 1 -> {
                    //return Field(x, y, from.getPlayer(), from.sign!!.getMinus(this.sign!!), false)
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
        }
        return false
    }
}