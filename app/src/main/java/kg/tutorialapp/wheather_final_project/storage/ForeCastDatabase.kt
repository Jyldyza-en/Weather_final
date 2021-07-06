package kg.tutorialapp.wheather_final_project.storage

import androidx.room.*
import kg.tutorialapp.wheather_final_project.models.ForeCast

@Database(
    entities = [ForeCast::class],
    version = 2,
    exportSchema = false
)

@TypeConverters(ModelsConverter::class, CollectionsConverter::class)
abstract class ForeCastDatabase: RoomDatabase() {
    abstract fun forecastDao(): ForeCastDao

    companion object{
        const val DB_NAME = "forecastDB"
    }
}