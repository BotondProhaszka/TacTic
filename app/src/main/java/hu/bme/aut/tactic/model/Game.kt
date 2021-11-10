package hu.bme.aut.tactic.model


import hu.bme.aut.tactic.activities.GameActivity
import hu.bme.aut.tactic.data.Score

enum class ROUND {INIT,FIRST_BASE, SEC_BASE, GAME, DRAW, BLUE_WIN, RED_WIN}

enum class PLAYER {
    RED, BLUE

}

enum class SIGN (val value: Int) {
    BASE(0),
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

public object Game {
        private var instance = Game

        private lateinit var map : ArrayList<ArrayList<Field>>

        private var mapWidth = 0
        private var mapHeight = 0
        private var round: ROUND = ROUND.FIRST_BASE
        private var actual_player : PLAYER = PLAYER.BLUE
        private var bluePlayer: PlayerUser? = null
        private var redPlayer: PlayerUser? = null

        private var score = Score(null, "Player1Name", 1, "Player2Name", 2, true)
        private var clickedFrom: Field? = null

        private lateinit var gameActivity: GameActivity

        fun getInstance(): Game {
            if (instance == null) {
                synchronized(this) {
                    instance = Game
                    return instance
                }
            }
            return instance
        }

        fun setPlayers(blue_name: String, red_name: String){
            bluePlayer = PlayerUser(blue_name, 0, PLAYER.BLUE)
            redPlayer = PlayerUser(red_name, 0, PLAYER.RED)
        }
        fun getBluePlayersName(): String{
            if(bluePlayer != null)
                return bluePlayer?.name.toString()
            return ""

        }
        fun getRedPlayersName(): String {
            if (redPlayer != null)
                return redPlayer?.name.toString()
            return ""

        }
        fun startNewGame(width: Int, height: Int){
            round = ROUND.FIRST_BASE
            setMap(width, height)
            setRandomFirstPlayer()
        }

        fun restartGame(){
            round = ROUND.FIRST_BASE
            setMap(mapWidth, mapHeight)
            setRandomFirstPlayer()

        }

        private fun setMap(width: Int, height: Int) {

            mapWidth = width
            mapHeight = height
            map = ArrayList()
            for(i in 0..width){
                val row = ArrayList<Field>(height)
                for (j in 0..height){
                    row.add(Field(i, j))
                }
                map.add(row)
            }
        }

        private fun setRandomFirstPlayer(){
            val rand = (0..1).random()
            actual_player = when(rand) {
                0 -> PLAYER.BLUE
                1 -> PLAYER.RED
                else -> PLAYER.RED
            }
        }
        fun setGameActivity(gameActivity: GameActivity){ this.gameActivity = gameActivity}

        @JvmName("getMap1")
        fun getMap() : ArrayList<ArrayList<Field>> {return map}
        fun getActualPlayer(): PLAYER { return actual_player }
        fun getMapWidth(): Int { return mapWidth }
        fun getMapHeight(): Int { return mapHeight }
        fun getScore(): Score { return score }

        private fun changePlayer(){
            actual_player = getOtherPlayer(actual_player)
            removeClickedFrom()
        }

        private fun getOtherPlayer(player: PLAYER): PLAYER{
            return when(player){
                PLAYER.RED -> PLAYER.BLUE
                PLAYER.BLUE -> PLAYER.RED
            }
        }

        fun playerWins(winner: PLAYER) {
            restartGame()
            gameActivity.gameOver(score)
        }

        fun clickedOn(x: Int, y: Int) {
            if(round != ROUND.FIRST_BASE && round != ROUND.SEC_BASE)
                if(!hasCorrectNeighbour(x,y)) {
                    if(clickedFrom != null)
                        removeClickedFrom()
                    return
                }
            when (round) {
                ROUND.FIRST_BASE -> {
                    map[x][y] = Field(x, y, actual_player, SIGN.BASE, false)
                    changePlayer()
                    round = ROUND.SEC_BASE
                    return
                }
                ROUND.SEC_BASE -> {
                    map[x][y] = Field(x, y, actual_player, SIGN.BASE, false)
                    changePlayer()
                    round = ROUND.GAME
                    return
                }
                ROUND.GAME -> {

                    if (map[x][y].isEmpty()) {
                        map[x][y] = Field(x, y, actual_player, SIGN.ONE, false)
                        changePlayer()
                        return
                    }
                    else if(clickedFrom == null){
                        if(map[x][y].getPlayer() == actual_player) {
                            map[x][y].setHighlighted(true)
                            clickedFrom = map[x][y]
                            return
                        }
                    }

                    else if(clickedFrom?.samePlace(map[x][y]) == true){
                        if(map[x][y].increaseSignValue())
                            changePlayer()
                        return
                    }
                    else if(clickedFrom?.getPlayer() == map[x][y].getPlayer()){
                        clickedFrom?.setHighlighted(false)
                        map[x][y].setHighlighted(true)
                        clickedFrom = map[x][y]
                        return
                    }
                    else{
                        if(map[x][y].attackFrom( Field(clickedFrom))) {
                            changePlayer()
                            return
                        }
                    }
                }
            }
        }

        private fun hasCorrectNeighbour(x: Int, y: Int): Boolean{
            if(x in 2..mapWidth)
                if(map[x-1][y].getPlayer() == actual_player)
                    return true
            if(x in 1 until mapWidth)
                if(map[x+1][y].getPlayer() == actual_player)
                    return true
            if(y in 2..mapHeight)
                if(map[x][y-1].getPlayer() == actual_player)
                    return true
            if(y in 1 until mapHeight)
                if(map[x][y+1].getPlayer() == actual_player)
                    return true
            return false
        }

        private fun removeClickedFrom(){
            clickedFrom?.setHighlighted(false)
            clickedFrom = null
        }

    }