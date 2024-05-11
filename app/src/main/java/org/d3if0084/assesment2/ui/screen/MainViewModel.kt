package org.d3if0084.assesment2.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import org.d3if0084.assesment2.database.PesanDao
import org.d3if0084.assesment2.model.Pesan


class MainViewModel(dao: PesanDao) : ViewModel() {
    val data: StateFlow<List<Pesan>> = dao.getPesan().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )
}