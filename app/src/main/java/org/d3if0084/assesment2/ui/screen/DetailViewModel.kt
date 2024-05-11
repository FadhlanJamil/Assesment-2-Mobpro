package org.d3if0084.assesment2.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3if0084.assesment2.database.PesanDao
import org.d3if0084.assesment2.model.Pesan

class DetailViewModel(private val dao: PesanDao) : ViewModel() {

    fun insert(nama: String, makandi: String, pesanan: String) {
        val pesan = Pesan(
            nama = nama,
            makandi = makandi,
            pesanan = pesanan
        )

        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(pesan)
        }
    }

    suspend fun getPesan(id: Long): Pesan? {
        return dao.getPesanById(id)
    }

    fun update(id: Long, nama: String, makandi: String, pesanan: String) {
        val pesan = Pesan(
            id = id,
            nama = nama,
            makandi = makandi,
            pesanan = pesanan
        )

        viewModelScope.launch(Dispatchers.IO) {
            dao.update(pesan)
        }
    }

    fun delete(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteById(id)
        }
    }
}