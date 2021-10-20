package hu.bme.aut.tactic.model

import android.util.Log

enum class ROUND {INIT,FIRST_BASE, SEC_BASE, GAME, DRAW, BLUE_WIN, RED_WIN}

enum class PLAYER {
    RED, BLUE

}

enum class SIGN {
    BASE, ONE, TWO, THREE, FOUR
}

object Game {
        private var instance = Game

        private lateinit var map : ArrayList<ArrayList<Field>>

        private var mapWidth = 0
        private var mapHeight = 0
        private var round: ROUND = ROUND.FIRST_BASE
        private var actual_player : PLAYER = PLAYER.RED

        private var clickedFrom: Field? = null

        fun getInstance(): Game {

            if (instance == null) {
                synchronized(this) {
                    instance = Game
                    return instance
                }
            }
            return instance
        }

        fun setMapSize(width: Int, height: Int) {
            mapWidth = width
            mapHeight = height
            map = ArrayList()
            for(i in 0..height){
                val row = ArrayList<Field>(width)
                for (j in 0..width){
                    row.add(Field(i, j))
                }
                map.add(row)
            }
        }

        fun setFirstPlayer(player: PLAYER){
            actual_player = player
        }

        @JvmName("getMap1")
        fun getMap() : ArrayList<ArrayList<Field>> {return map}

        fun getMapWidth(): Int {
            return mapWidth
        }

        fun getMapHeight(): Int {
            return mapHeight
        }

        fun changePlayer(){
            actual_player = when(actual_player){
                PLAYER.BLUE -> PLAYER.RED
                PLAYER.RED -> PLAYER.BLUE
            }
        }

        fun getOtherPlayer(player: PLAYER): PLAYER{
            return when(player){
                PLAYER.RED -> PLAYER.BLUE
                PLAYER.BLUE -> PLAYER.RED
            }
        }

        fun clickedOn(x: Int, y: Int): Field {
            var result = Field(x, y, actual_player, SIGN.FOUR, false)
            when (round) {
                ROUND.FIRST_BASE -> {


                    map[x][y] = Field(x, y, actual_player, SIGN.BASE, false)
                    changePlayer()
                    round = ROUND.SEC_BASE
                    return map[x][y]
                }
                ROUND.SEC_BASE -> {
                    result = Field(x, y, actual_player, SIGN.BASE, false)
                    map[x][y] = result
                    changePlayer()
                    round = ROUND.GAME
                    return result
                }
                ROUND.GAME -> {
                    if (map[x][y].isEmpty()) {
                        Log.d("Bugfix", "GAME")
                        result = Field(x, y, actual_player, SIGN.ONE, false)
                        map[x][y] = result
                        changePlayer()
                        return result
                    }


                }

            }

            return result
        }


    }