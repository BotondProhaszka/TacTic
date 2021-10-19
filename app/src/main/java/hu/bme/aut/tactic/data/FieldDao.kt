package hu.bme.aut.tactic.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FieldDao {
    @Query("SELECT * FROM fieldDc")
    fun getAllFieldDc () : List<FieldDc>

    @Query("SELECT * FROM fieldDc WHERE fieldDcId = :id")
    fun getFieldDcById(id: Long) : List<FieldDc>

    @Insert
    fun insterFieldDc(fieldDcs: FieldDc)

    @Delete
    fun deleteFieldDc(fieldDc: FieldDc)

}