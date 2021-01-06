package org.d3if0130.mobpro2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.d3if0130.mobpro2.data.Mahasiswa
import org.d3if0130.mobpro2.data.MahasiswaDao

class MainViewModel(private val db : MahasiswaDao) : ViewModel() {
    val data = db.getData()
    fun insertData(mahasiswa: Mahasiswa) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                db.insertData(mahasiswa)
            }
        }
    }
    fun deleteData(ids: List<Int>) {
        val newIds = ids.toList()
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                db.deleteData(newIds)
            }
        }
    }
}
