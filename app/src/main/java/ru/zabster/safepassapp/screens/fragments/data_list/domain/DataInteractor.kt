package ru.zabster.safepassapp.screens.fragments.data_list.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import ru.zabster.safepassapp.db.AppDatabase
import ru.zabster.safepassapp.db.entities.CategoryEntity
import ru.zabster.safepassapp.screens.base.domain.DataListMapper
import ru.zabster.safepassapp.screens.base.data.CategoryData
import ru.zabster.safepassapp.screens.fragments.data_list.data.CredentialData
import ru.zabster.safepassapp.screens.fragments.data_list.data.DataListScreenState

/**
 * Data use case for data list fragment
 *
 * @param db app database
 * @param dispatcher io dispatcher
 */
class DataInteractor(
    private val db: AppDatabase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    private val dataMapper = DataListMapper()

    /**
     * List user categories
     */
    val categoryListLiveData: LiveData<List<CategoryData>>
        get() = Transformations.map(
            db.categoryDao().getCategoriesLiveData(),
            dataMapper::mapCategories
        )

    /**
     * Get selected category id
     *
     * @return [Long] category id
     */
    suspend fun getSelectedCategoryId(): Long = withContext(dispatcher) {
        db.categoryDao().getSelectedCategoryId() ?: CategoryEntity.TableInfo.DefaultValues.COLUMN_ID_DEF_VALUE
    }

    /**
     * Get credential list only selected category
     *
     * @param categoryId selected category
     *
     * @return list credentials
     */
    suspend fun getCredentialListByCategory(categoryId: Long?): List<CredentialData> =
        withContext(dispatcher) {
            (if (categoryId == CategoryEntity.TableInfo.DefaultValues.COLUMN_ID_DEF_VALUE)
                db.sigInDao().getCredentials()
            else db.sigInDao().getCredentialsByCategoryId(
                categoryId ?: CategoryEntity.TableInfo.DefaultValues.COLUMN_ID_DEF_VALUE
            )).map(dataMapper::mapCredential)
        }

    /**
     * Check having data
     *
     * @return screen state
     */
    suspend fun haveData(): DataListScreenState =
        withContext(dispatcher) {
            if (db.sigInDao().count() > 0) DataListScreenState.NotEmptyList
            else DataListScreenState.EmptyList
        }

    /**
     * Update category
     *
     * @param data category ui data
     */
    suspend fun updateCategory(data: CategoryData) {
        withContext(dispatcher) {
            db.categoryDao().update(dataMapper.mapCategoryToDb(data))
        }
    }

    /**
     * Delete category and reset it in other credentials
     *
     * @param data category ui data
     */
    suspend fun deleteCategory(data: CategoryData) {
        withContext(dispatcher) {
            db.categoryDao().deleteById(data.id)
            if (data.isSelect)
                db.categoryDao()
                    .resetSelectedCategory(CategoryEntity.TableInfo.DefaultValues.COLUMN_ID_DEF_VALUE)
            db.sigInDao()
                .updateCategoryToDefault(
                    data.id,
                    CategoryEntity.TableInfo.DefaultValues.COLUMN_ID_DEF_VALUE
                )
        }
    }

    /**
     * Delete user credential
     *
     * @param credentialData data for deleting
     */
    suspend fun deleteCredential(credentialData: CredentialData) {
        withContext(dispatcher) {
            db.sigInDao().deleteById(credentialData.id)
        }
    }
}
