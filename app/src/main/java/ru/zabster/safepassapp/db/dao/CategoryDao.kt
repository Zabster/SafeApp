package ru.zabster.safepassapp.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query

import ru.zabster.safepassapp.db.entities.CategoryEntity

/**
 * Access to category object
 */
@Dao
abstract class CategoryDao : BaseDao<CategoryEntity>() {

    /**
     * Get list categories as LiveData
     *
     * @return [LiveData] list categories
     */
    @Query("select * from category")
    abstract fun getCategoriesLiveData(): LiveData<List<CategoryEntity>>

    /**
     * Get selected category id
     *
     * @return [Long] category id
     */
    @Query("select id from category where is_selected=1")
    abstract suspend fun getSelectedCategoryId(): Long?

    /**
     * Get category by id
     *
     * @param id category id from db
     *
     * @return [CategoryEntity] category info
     */
    @Query("select * from category where id=:id")
    abstract suspend fun getCategoryById(id: Long): CategoryEntity?

    /**
     * Delete category by id
     */
    @Query("delete from category where id=:categoryId")
    abstract suspend fun deleteById(categoryId: Long)

    /**
     * Set select category by id
     */
    @Query("update category set is_selected=1 where id=:checkedCategoryId")
    abstract suspend fun resetSelectedCategory(checkedCategoryId: Long)
}