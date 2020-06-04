package com.example.crossie.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*



@Database(entities = [ClueTable::class], version = 1)
abstract class ClueDatabase: RoomDatabase() {
    abstract val clueDao: ClueDao
    companion object {
        @Volatile
        private var INSTANCE: ClueDatabase? = null
        fun getDatabase(context: Context): ClueDatabase {
            synchronized(ClueDatabase::class.java) {
                var instance = INSTANCE
                if(instance == null){
                    instance = Room.databaseBuilder(context.applicationContext, ClueDatabase::class.java, "cluesdb").fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

}

@Dao
interface ClueDao {
    @Query("select * from clues where solved = 0")
    fun getUnsolvedClues(): List<ClueTable>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(clues: List<ClueTable>)

    @Insert()
    fun insert(clue: ClueTable)

    @Query("update clues set solved = 1 where clue = :clue")
    fun updateSolved(clue: String)

    /*
    @Query("select firstTime from userlog")
    fun isFirstTime(): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateName(user: UserName)

    @Query("select userName from userlog")
    fun getUserName(): String

    @Query("select * from userlog")
    fun getUserLog(): List<UserName>
    */
}

