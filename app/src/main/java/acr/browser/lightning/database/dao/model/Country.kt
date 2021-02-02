package acr.browser.lightning.database.dao.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Blob

@Entity(tableName = "country")
data class Country(
        @PrimaryKey(autoGenerate = true)
        var id: Int,
        @ColumnInfo(name = "name", ) var name: String?,
        @ColumnInfo(name = "code") var code: String?,
)