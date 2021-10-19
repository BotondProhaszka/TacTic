package hu.bme.aut.tactic.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import hu.bme.aut.tactic.model.Field

@Entity(tableName = "fieldDc")
data class FieldDc(
    @PrimaryKey(autoGenerate = true) var fieldDcId: Long?,
    @ColumnInfo(name = "owner_field") var owner_field: Field
)