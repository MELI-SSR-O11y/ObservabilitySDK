package com.example.data.database.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE screen ADD COLUMN isSync INTEGER NOT NULL DEFAULT 0")
        db.execSQL("ALTER TABLE incident_tracker ADD COLUMN isSync INTEGER NOT NULL DEFAULT 0")
        db.execSQL("ALTER TABLE metadata ADD COLUMN isSync INTEGER NOT NULL DEFAULT 0")
    }
}
