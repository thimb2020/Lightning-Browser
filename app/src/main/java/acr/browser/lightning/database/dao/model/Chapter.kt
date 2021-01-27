package acr.browser.lightning.database.dao.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Blob

@Entity(tableName = "chapter")
data class Chapter(
        @PrimaryKey(autoGenerate = true)
        var id: Int,
        @ColumnInfo(name = "name", ) var name: String?,
        @ColumnInfo(name = "story_id") var start: Int?,
        @ColumnInfo(name = "url") var icon_name: String?,

)