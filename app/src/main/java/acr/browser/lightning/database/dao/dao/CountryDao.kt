package acr.browser.lightning.database.dao.dao

import acr.browser.lightning.database.dao.model.Category
import acr.browser.lightning.database.dao.model.Country
import androidx.room.Dao
import androidx.room.Query

@Dao
interface CountryDao {
    @Query("SELECT * FROM country")
    fun getAll(): List<Country>
    @Query("SELECT * FROM country where code=:code")
    fun findByCode(code : String): Country
    @Query("SELECT * FROM country where id=:id")
    fun findById(id : Int): Country
}