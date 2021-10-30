package hu.bme.aut.tactic.data

import android.content.Context
import androidx.room.*

@Database(entities = [Score::class], version = 1, exportSchema = false)
abstract class ScoresDatabase : RoomDatabase() {
    abstract fun scoreDao(): ScoreDao

    companion object {
        @Volatile
        private var INSTANCE: ScoresDatabase? = null

        fun getDatabase(applicationContext: Context): ScoresDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    applicationContext,
                    ScoresDatabase::class.java,
                    "scores_db"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}