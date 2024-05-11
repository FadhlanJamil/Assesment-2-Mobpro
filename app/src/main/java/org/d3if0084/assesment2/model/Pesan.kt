package org.d3if0084.assesment2.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pesan")
data class Pesan(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val nama: String,
    val makandi: String,
    val pesanan: String
)