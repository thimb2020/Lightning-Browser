package acr.browser.lightning.database.dao.dao

import acr.browser.lightning.database.dao.model.Book
import acr.browser.lightning.database.dao.model.Category
import androidx.room.Dao
import androidx.room.Query

@Dao
interface BookDao {
    @Query("SELECT * FROM story where category_id=:categoryId")
    fun getBooksByCategory(categoryId: Int): List<Book>
}