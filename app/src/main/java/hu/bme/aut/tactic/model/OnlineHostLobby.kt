package hu.bme.aut.tactic.model

import java.util.*

data class OnlineHostLobby(val lobbyName: String, val hostPlayerName: String ,val width: Int, val height: Int) {
    var id = Random().nextInt(1000)


    constructor(id: Int, lobbyName: String, hostPlayerName: String, width: Int, height: Int) : this(lobbyName, hostPlayerName, width, height){
        this.id = id
    }

    fun getConnString(): String = "${lobbyName}_$id"
}