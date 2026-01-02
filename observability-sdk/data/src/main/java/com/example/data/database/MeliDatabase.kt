package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.database.daos.IncidentDao
import com.example.data.database.daos.MetadataDao
import com.example.data.database.daos.ScreenDao
import com.example.data.database.entities.IncidentTrackerEntity
import com.example.data.database.entities.MetadataEntity
import com.example.data.database.entities.ScreenEntity

@Database(
  entities = [ScreenEntity::class, IncidentTrackerEntity::class, MetadataEntity::class], version = 1
)
abstract class MeliDatabase: RoomDatabase() {

  abstract fun screensDao() : ScreenDao
  abstract fun incidentDao() : IncidentDao
  abstract fun metadataDao() : MetadataDao

  companion object {
    @Volatile
    private var instance : MeliDatabase? = null

    @Synchronized
    fun getInstance(context : Context) : MeliDatabase {
      if(instance == null) {
        instance = Room.databaseBuilder(
          context.applicationContext, MeliDatabase::class.java, "MELI"
        ).build()
      }
      return instance!!
    }
  }
}