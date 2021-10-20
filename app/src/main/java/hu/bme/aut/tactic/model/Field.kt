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

    fun getPlayer() : PLAYER? { return player }
    fun getSign() : SIGN? { return sign }
    fun getHighlighted() : Boolean { return highlighted }

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

    fun isEmpty() : Boolean {return (player == null)}

}