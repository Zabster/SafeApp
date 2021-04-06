package ru.zabster.safepassapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.zabster.safepassapp.db.dao.CategoryDao
import ru.zabster.safepassapp.db.dao.CredentialDao
import ru.zabster.safepassapp.db.entities.CategoryEntity
import ru.zabster.safepassapp.db.entities.CredentialEntity

private const val DB_VERSION = 1

/**
 * App DB
 */
@Database(entities = [CredentialEntity::class, CategoryEntity::class], version = DB_VERSION)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Access to categories
     */
    abstract fun categoryDao(): CategoryDao

    /**
     * Access to user credentials
     */
    abstract fun sigInDao(): CredentialDao
}