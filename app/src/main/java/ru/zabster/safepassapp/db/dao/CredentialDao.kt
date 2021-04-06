package ru.zabster.safepassapp.db.dao

import androidx.room.Dao
import androidx.room.Query

import ru.zabster.safepassapp.db.entities.CredentialEntity

/**
 * Access to user credential data
 */
@Dao
abstract class CredentialDao : BaseDao<CredentialEntity>() {

    /**
     * Get list credentials
     *
     * @return list credentials
     */
    @Query("select * from credentials")
    abstract suspend fun getCredentials(): List<CredentialEntity>

    /**
     * Get list credentials only one category
     *
     * @param categoryId id category for sort
     *
     * @return list credentials
     */
    @Query("select * from credentials where categoryId=:categoryId")
    abstract suspend fun getCredentialsByCategoryId(categoryId: Long): List<CredentialEntity>

    /**
     * Count user data
     *
     * @return [Int] count
     */
    @Query("select count(*) from credentials")
    abstract suspend fun count(): Int

    /**
     * Set default category to credentials when delete category
     *
     * @param categoryId deleted category id
     * @param defCategoryId category id for switching
     */
    @Query("update credentials set categoryId=:defCategoryId where categoryId=:categoryId")
    abstract suspend fun updateCategoryToDefault(categoryId: Long, defCategoryId: Long)

    /**
     * Delete credential by id
     *
     * @param id credential id
     */
    @Query("delete from credentials where id=:id")
    abstract suspend fun deleteById(id: Long)
}