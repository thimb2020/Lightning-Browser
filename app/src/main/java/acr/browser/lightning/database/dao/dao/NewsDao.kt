package acr.browser.lightning.database.dao.dao

import acr.browser.lightning.database.dao.model.Category
import acr.browser.lightning.database.dao.model.Link
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

@Dao
interface NewsDao {
    @Query("SELECT * FROM category")
    fun findAllCategory(): List<Category>

    @Query("SELECT * FROM category where start=1 limit 1")
    fun getStartCategory(): Category

    @Query("SELECT * FROM link where category_id=:categoryId")
    fun findLinksByCategory(categoryId: Int): List<Link>

    @Query("SELECT * FROM link where category_id in (select id from category where start=1)")
    fun findLinksStart(): List<Link>

    @Query("SELECT * FROM link")
    fun findAllLink(): List<Link>

    @Transaction
    @Query("update category set start=0 where start=1")
    fun clearStartCategory()
    @Transaction
    @Query("update category set start=1 where id=:categoryId")
    fun updateStartCategory(categoryId: Int)
    @Update
    fun updateCategory(vararg category: Category)
}