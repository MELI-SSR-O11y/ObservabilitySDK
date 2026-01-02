package com.example.data.database.daos

import androidx.room.Upsert

interface GenericDao<T> {

  @Upsert
  suspend fun upsert(entity: T)

  @Upsert
  suspend fun upsertAll(entities: List<T>)

}