package hu.bme.aut.tactic.model

enum class PLAYER_ON_FIELD {
    EMPTY, RED, BLUE
}
enum class FIELD_HIGHLITED {
    TRUE, FALSE
}

class Field{
    private lateinit var fieldStateObject : FieldStateObject

    private var playerOnField: PLAYER_ON_FIELD = PLAYER_ON_FIELD.EMPTY
    private var highlited: FIELD_HIGHLITED = FIELD_HIGHLITED.FALSE

    fun getPlayerOnField() : PLAYER_ON_FIELD{
        return playerOnField
    }

    fun setFieldStateObject(fddo: FieldStateObject){
        fieldStateObject = fddo
    }
    fun getFieldStateObject() :FieldStateObject{
        return fieldStateObject
    }
}