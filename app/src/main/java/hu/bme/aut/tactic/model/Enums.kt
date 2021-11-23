package hu.bme.aut.tactic.model

enum class ROUND {FIRST_BASE, SEC_BASE, GAME}

enum class PLAYER {
    RED, BLUE

}

enum class SIGN (val value: Int) {
    BASE(2),
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6);
    fun getMinus(rightSide: SIGN): SIGN {
        return when(this.value - rightSide.value){
            0 -> ONE
            1 -> ONE
            2 -> TWO
            3 -> THREE
            4 -> FOUR
            5 -> FIVE
            else -> ONE
        }
    }
    fun minus(rightSide: SIGN): Int {
        return this.value - rightSide.value
    }

    fun getNext() : SIGN {
        return when(value){
            0 -> ONE
            1 -> TWO
            2 -> THREE
            3 -> FOUR
            4 -> FIVE
            5 -> SIX
            else -> BASE
        }
    }
}
