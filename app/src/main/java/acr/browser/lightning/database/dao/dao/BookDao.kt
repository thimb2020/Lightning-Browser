package acr.browser.lightning.database.dao.dao

import acr.browser.lightning.database.dao.model.Book
import acr.browser.lightning.database.dao.model.Category
import acr.browser.lightning.database.dao.model.Chapter
import androidx.room.Dao
import androidx.room.Query

@Dao
interface BookDao {
    @Query("SELECT * FROM story where category_id=:categoryId")
    fun getBooksByCategory(categoryId: Int): List<Book>
    @Query("SELECT * FROM chapter where story_id = (SELECT id FROM story where  start=1 ORDER BY ROWID ASC LIMIT 1)")
    fun getStartChapters(): List<Chapter>
}