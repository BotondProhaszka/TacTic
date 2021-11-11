package hu.bme.aut.tactic.model


class PlayerUser (var name: String,var colour: PLAYER) {
    private var _score: Int = 0
    var score: Int
        get() = _score
        set(value) {
            _score = value
        }

}