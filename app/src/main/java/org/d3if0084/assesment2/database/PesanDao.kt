package org.d3if0084.assesment2.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import org.d3if0084.assesment2.model.Pesan

@Dao
interface PesanDao {

    @Insert
    suspend fun insert(pesan: Pesan)

    @Update
    suspend fun update(pesan: Pesan)

    @Query("SELECT * FROM pesan ORDER BY id ASC")
    fun getPesan(): Flow<List<Pesan>>

    @Query("SELECT * FROM pesan WHERE id = :id")
    suspend fun getPesanById(id: Long): Pesan?

    @Query("DELETE FROM pesan WHERE id = :id")
    suspend fun deleteById(id: Long)
}