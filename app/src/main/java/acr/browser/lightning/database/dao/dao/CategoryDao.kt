package acr.browser.lightning.database.dao.dao

import acr.browser.lightning.database.dao.model.Category
import androidx.room.Dao
import androidx.room.Query

@Dao
interface CategoryDao {
    @Query("SELECT * FROM category")
    fun getAll(): List<Category>
}