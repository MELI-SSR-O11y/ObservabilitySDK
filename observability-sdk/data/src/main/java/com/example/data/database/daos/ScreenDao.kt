package com.example.data.database.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.data.database.entities.ScreenEntity
import com.example.data.database.relations.ScreenWithIncidents
import kotlinx.coroutines.flow.Flow

@Dao
interface ScreenDao: GenericDao<ScreenEntity> {

  @Transaction
  @Query("SELECT * FROM screen")
  fun getScreensWithRelations(): Flow<List<ScreenWithIncidents>>

  @Query("SELECT EXISTS(SELECT 1 FROM screen WHERE name = :screen)")
  suspend fun existByName(screen: String): Boolean

  @Query("SELECT * FROM screen WHERE isSync = 0")
  suspend fun getAllNotSync(): List<ScreenEntity>

  @Query("SELECT id FROM screen WHERE name = :screen")
  suspend fun getIdByName(screen: String): String?

}
