package org.d3if0084.assesment2.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.d3if0084.assesment2.model.Pesan
import kotlin.concurrent.Volatile

@Database(entities = [Pesan::class], version = 1, exportSchema = false)
abstract class PesanDb : RoomDatabase() {

    abstract val dao: PesanDao

    companion object {

        @Volatile
        private var INSTANCE: PesanDb? = null

        fun getInstance(context: Context): PesanDb {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        PesanDb::class.java,
                        "pesan.db"
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }

    }
}