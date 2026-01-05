package com.example.data.database.daos

import androidx.room.Dao
import androidx.room.Query
import com.example.data.database.entities.IncidentTrackerEntity

@Dao
interface IncidentDao: GenericDao<IncidentTrackerEntity> {

  @Query("SELECT * FROM incident_tracker WHERE isSync = 0")
  suspend fun getAllNotSync(): List<IncidentTrackerEntity>
}