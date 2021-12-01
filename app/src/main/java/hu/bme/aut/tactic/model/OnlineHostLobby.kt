package hu.bme.aut.tactic.model


class OnlineHostLobby(val lobbyName: String, val hostPlayerName: String, val size: Int, val firstPlayer: PLAYER) {
    var joinPlayerName: String = ""

    constructor() : this("", "", 0, PLAYER.BLUE){}



    fun getConnString(): String = "${lobbyName}_${hostPlayerName}_${size}_${size}"
}