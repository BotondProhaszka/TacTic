package hu.bme.aut.tactic.model

data class OnlineGameTransferObj(var redName : String) {
     constructor() : this("") {}
     var blueName: String = ""

     var x: Int = 0
     var y: Int = 0

     var lastPlayer: String = ""
     var plsStep = 0
}