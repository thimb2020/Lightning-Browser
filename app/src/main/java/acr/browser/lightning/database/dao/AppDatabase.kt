package acr.browser.lightning.database.dao

import acr.browser.lightning.database.dao.dao.CategoryDao
import acr.browser.lightning.database.dao.model.Category
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
@Database(entities = [Category::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract val categoryDao: CategoryDao?

    companion object {
        @Volatile private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context)= instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also { instance = it}
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(context,
                AppDatabase::class.java, "usnews.db").createFromAsset("usnews.db").fallbackToDestructiveMigration().allowMainThreadQueries()
                .build()
    }
}