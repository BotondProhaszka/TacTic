package hu.bme.aut.tactic.Firebase

class Task {
    companion object Factory{
        fun create(): Task = Task()
    }

    var objectId: String = "objectId"
    var taskDesc: String = "desc"
    var done: Boolean? = false
}