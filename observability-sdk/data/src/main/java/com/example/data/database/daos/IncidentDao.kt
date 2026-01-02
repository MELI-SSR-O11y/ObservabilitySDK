package com.example.data.database.daos

import androidx.room.Dao
import com.example.data.database.entities.IncidentTrackerEntity

@Dao
interface IncidentDao: GenericDao<IncidentTrackerEntity>