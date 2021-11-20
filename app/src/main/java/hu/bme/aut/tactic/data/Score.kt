package hu.bme.aut.tactic.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import hu.bme.aut.tactic.model.PLAYER

@Entity(tableName ="scores")
data class Score(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) var id: Long? = null,
    @ColumnInfo(name = "player1_name") var player1Name: String,
    @ColumnInfo(name = "player1_score") var player1Score: Int,
    @ColumnInfo(name = "player2_name") var player2Name: String,
    @ColumnInfo(name = "player2_score") var player2Score: Int,
    @ColumnInfo(name = "offlineGame") var offlineGame: Boolean,
    @ColumnInfo(name = "winner") var winner: PLAYER?
)