package ru.zabster.safepassapp.di.impls.room

import android.content.ContentValues
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory

import ru.zabster.safepassapp.db.AppDatabase
import ru.zabster.safepassapp.db.entities.CategoryEntity

/**
 * Get private db
 *
 * @param context the context for the database. This is usually the Application context.
 * @param password db secret word
 * @param migrations list of modify database
 */
class RoomImpl(
    private val context: Context,
    private val password: String,
    private val migrations: Array<Migration>
) : RoomHelper {

    override fun getSecureDataBase(): AppDatabase = Room
        .databaseBuilder(context, AppDatabase::class.java, DATA_BASE_NAME)
        .addMigrations(*migrations)
        .addCallback(callback)
        .openHelperFactory(SupportFactory(SQLiteDatabase.getBytes(password.toCharArray())))
        .build()

    private val callback = object : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            insertDefaultCategory(db)
        }
    }

    private fun insertDefaultCategory(db: SupportSQLiteDatabase) {
        val cv = ContentValues().apply {
            put(
                CategoryEntity.TableInfo.Columns.COLUMN_NAME,
                CategoryEntity.TableInfo.DefaultValues.COLUMN_NAME_DEF_VALUE
            )
            put(CategoryEntity.TableInfo.Columns.COLUMN_IS_SELECTED, true)
        }
        db.insert(CategoryEntity.TABLE_NAME, SQLiteDatabase.CONFLICT_IGNORE, cv)
    }

    companion object {

        private const val DATA_BASE_NAME = "SPA_DB"
    }
}
