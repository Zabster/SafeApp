package ru.zabster.safepassapp.screens.fragments.new_data.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.zabster.safepassapp.db.AppDatabase

import ru.zabster.safepassapp.db.entities.CategoryEntity
import ru.zabster.safepassapp.db.entities.CredentialEntity
import ru.zabster.safepassapp.screens.base.data.CategoryData
import ru.zabster.safepassapp.screens.base.domain.DataListMapper
import ru.zabster.safepassapp.utils.secure.SecurityUtils

/**
 * Credential save interactor
 *
 * @param db app database
 * @param dispatcher coroutine dispatcher
 */
class CreateDataInteractor(
    private val db: AppDatabase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    private val mapper = DataListMapper()

    /**
     * Save new credential
     *
     * @param title title text
     * @param desc description text
     * @param pass pass text
     * @param categoryId category id from [CategoryEntity]
     *
     * @return save or not
     */
    suspend fun saveCredential(title: String, desc: String, pass: String, categoryId: Long): Boolean =
        withContext(dispatcher) {
            SecurityUtils.getNewSecret(pass)?.let { secureKeyData ->
                db.sigInDao().insertOrUpdate(
                    CredentialEntity(
                        name = title,
                        description = desc,
                        hashPass = SecurityUtils.secureKeyDataToGson(secureKeyData),
                        categoryId = categoryId
                    )
                )
                true
            } ?: false
        }

    /**
     * Get category data from db by id
     *
     * @param categoryId category id
     *
     * @return ui category data
     */
    suspend fun getCategoryById(categoryId: Long): CategoryData? =
        withContext(dispatcher) {
            db.categoryDao()
                .getCategoryById(categoryId)
                ?.let { categoryEntity -> mapper.mapCategory(categoryEntity) }
        }
}
