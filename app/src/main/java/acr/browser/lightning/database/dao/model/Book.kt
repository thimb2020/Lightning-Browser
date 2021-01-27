package acr.browser.lightning.database.dao.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Blob

@Entity(tableName = "story")
data class Book(
        @PrimaryKey(autoGenerate = true)
        var id: Int,
        @ColumnInfo(name = "name", ) var name: String?,
        @ColumnInfo(name = "category_id") var category_id: String?,
        @ColumnInfo(name = "start") var start: Int?,
)