package hu.bme.aut.tactic.model


class OnlineHostLobby(val lobbyName: String, val hostPlayerName: String, val width: Int, val height: Int, val firstPlayer: PLAYER) {
    var joinPlayerName: String = ""

    constructor() : this("", "", 0, 0, PLAYER.BLUE){}



    fun getConnString(): String = "${lobbyName}_${hostPlayerName}_${width}_${height}"
}