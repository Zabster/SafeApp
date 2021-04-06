package ru.zabster.safepassapp.screens.base.domain

import ru.zabster.safepassapp.db.entities.CategoryEntity
import ru.zabster.safepassapp.db.entities.CredentialEntity
import ru.zabster.safepassapp.screens.base.data.CategoryData
import ru.zabster.safepassapp.screens.fragments.data_list.data.CredentialData

/**
 * Mapper for db tables to Data models
 */
class DataListMapper {

    /**
     * Map categories list
     *
     * @param dataFromDb categories list from db
     *
     * @return list of view categories
     */
    fun mapCategories(dataFromDb: List<CategoryEntity>): List<CategoryData> =
        dataFromDb.toMutableList().map(::mapCategory)

    /**
     * Map categories list
     *
     * @param data categories list from db
     *
     * @return list of view categories for db
     */
    fun mapCategoryToDb(data: CategoryData): CategoryEntity =
        CategoryEntity(id = data.id, name = data.name, isSelected = data.isSelect)

    /**
     * Map credential
     *
     * @param dataFromDb credential from db
     *
     * @return credential data for view
     */
    fun mapCredential(dataFromDb: CredentialEntity): CredentialData =
        CredentialData(
            id = dataFromDb.id,
            name = dataFromDb.name,
            description = dataFromDb.description,
            hashPass = dataFromDb.hashPass,
            categoryId = dataFromDb.categoryId,
            pass = ""
        )

    /**
     * Map category
     *
     * @param dataFromDb category from db
     *
     * @return category data for view
     */
    fun mapCategory(dataFromDb: CategoryEntity): CategoryData =
        CategoryData(
            id = dataFromDb.id,
            name = dataFromDb.name,
            isSelect = dataFromDb.isSelected,
            canDelete = dataFromDb.id != CategoryEntity.TableInfo.DefaultValues.COLUMN_ID_DEF_VALUE
        )
}
