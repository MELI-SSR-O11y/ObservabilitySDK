package com.example.data.database.daos

import androidx.room.Dao
import androidx.room.Query
import com.example.data.database.entities.MetadataEntity

@Dao
interface MetadataDao: GenericDao<MetadataEntity> {

  @Query("SELECT * FROM metadata WHERE isSync = 0")
  suspend fun getAllNotSync(): List<MetadataEntity>

}