package ru.zabster.safepassapp.di.impls.room

import ru.zabster.safepassapp.db.AppDatabase

/**
 * Help create database
 */
interface RoomHelper {

    /**
     * Get encryption data base
     */
    fun getSecureDataBase(): AppDatabase

}