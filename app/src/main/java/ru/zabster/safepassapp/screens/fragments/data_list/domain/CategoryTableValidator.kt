package ru.zabster.safepassapp.screens.fragments.data_list.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.zabster.safepassapp.db.dao.CategoryDao
import ru.zabster.safepassapp.db.entities.CategoryEntity
import ru.zabster.safepassapp.screens.base.other.DatabaseValidator

/**
 * Validate creating default values
 *
 * @param categoryDao check category integrity
 * @param dispatcher io dispatcher
 */
class CategoryTableValidator(
    private val categoryDao: CategoryDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : DatabaseValidator {

    override suspend fun validateAndRepair() {
        withContext(dispatcher) {
            categoryDao.getCategoryById(
                CategoryEntity.TableInfo.DefaultValues.COLUMN_ID_DEF_VALUE
            ) ?: run {
                categoryDao.insertOrUpdate(
                    CategoryEntity(
                        id = CategoryEntity.TableInfo.DefaultValues.COLUMN_ID_DEF_VALUE,
                        name = CategoryEntity.TableInfo.DefaultValues.COLUMN_NAME_DEF_VALUE,
                        isSelected = true
                    )
                )
            }
        }
    }
}