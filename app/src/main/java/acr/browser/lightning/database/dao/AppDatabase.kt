package acr.browser.lightning.database.dao

import acr.browser.lightning.database.dao.dao.NewsDao
import acr.browser.lightning.database.dao.dao.CountryDao
import acr.browser.lightning.database.dao.model.Category
import acr.browser.lightning.database.dao.model.Link
import acr.browser.lightning.database.dao.model.Country
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Copyright:MyApplication
 * Author: liyang <br></br>
 * Date:2018/6/15 下午5:07<br></br>
 * Desc: <br></br>
 */
@Database(entities = [Category::class, Link::class, Country::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract val newsDao: NewsDao?
    abstract val countryDao: CountryDao?

    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(context,
                AppDatabase::class.java, "sites.db").createFromAsset("sites.db")
                //.fallbackToDestructiveMigration().allowMainThreadQueries()
                .allowMainThreadQueries()
                .build()
    }
}