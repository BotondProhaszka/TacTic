package hu.bme.aut.tactic.data

import androidx.room.*

@Dao
interface ScoreDao {
    @Query("SELECT * FROM scores")
    fun getAll(): List<Score>

    @Query("SELECT * FROM scores WHERE offlineGame = 'true'")
    fun getOfflineList(): List<Score>

    @Query("SELECT * FROM scores WHERE offlineGame = 'false'")
    fun getOnlineList(): List<Score>

    @Insert
    fun insert(score: Score): Long

    @Update
    fun update(score: Score)

    @Delete
    fun deleteItem(score: Score)

    @Query("DELETE FROM scores")
    fun deleteAll()
}