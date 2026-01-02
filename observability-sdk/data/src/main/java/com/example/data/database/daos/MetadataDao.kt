package com.example.data.database.daos

import androidx.room.Dao
import com.example.data.database.entities.MetadataEntity

@Dao
interface MetadataDao: GenericDao<MetadataEntity>