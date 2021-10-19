package hu.bme.aut.tactic.model

import android.os.DropBoxManager
import android.util.Log
import hu.bme.aut.tactic.fragments.PLAYER
import hu.bme.aut.tactic.fragments.SIGN

enum class ROUND {INIT,RED_BASE, BLUE_BASE, GAME, DRAW, BLUE_WIN, RED_WIN}

object Game {
        private var instance = Game

        lateinit var map : ArrayList<ArrayList<Field>>

        private var mapWidth = 0
        private var mapHeight = 0
        private var round: ROUND = ROUND.INIT
        private var next_player : PLAYER = PLAYER.RED

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
                    Log.d("Bugfix", "${i}:${j}")
                    row.add(Field())
                }
                map.add(row)
            }
        }

        fun getMapWidth(): Int {
            return mapWidth
        }

        fun getMapHeight(): Int {
            return mapHeight
        }


        fun clickedOn(x: Int, y: Int): FieldStateObject{
            when(next_player){
                PLAYER.RED-> {
                    map[x][y].setFieldStateObject(FieldStateObject(next_player, SIGN.BASE))
                    next_player = PLAYER.BLUE
                }
                PLAYER.BLUE-> {
                    map[x][y].setFieldStateObject(FieldStateObject(next_player, SIGN.BASE))
                    next_player = PLAYER.RED
                }
            }

            return map[x][y].getFieldStateObject()
        }
    }