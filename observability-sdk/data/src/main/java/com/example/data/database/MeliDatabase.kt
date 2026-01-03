package com.example.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.database.daos.IncidentDao
import com.example.data.database.daos.MetadataDao
import com.example.data.database.daos.ScreenDao
import com.example.data.database.entities.IncidentTrackerEntity
import com.example.data.database.entities.MetadataEntity
import com.example.data.database.entities.ScreenEntity

@Database(
  entities = [ScreenEntity::class, IncidentTrackerEntity::class, MetadataEntity::class], 
  version = 2
)
abstract class MeliDatabase: RoomDatabase() {

  abstract fun screensDao() : ScreenDao
  abstract fun incidentDao() : IncidentDao
  abstract fun metadataDao() : MetadataDao

}