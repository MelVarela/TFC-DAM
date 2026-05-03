package com.example.notasmazmorras.data.repositories

import com.example.notasmazmorras.data.model.local.SysTable
import com.example.notasmazmorras.data.repositories.daos.SysTableDao
import kotlinx.coroutines.flow.Flow

interface SystemRepository {

    suspend fun insert(data : SysTable)

    suspend fun update(data : SysTable)

    fun getLastUser(): Flow<String>

    fun getLanguage(): Flow<String>

    fun getLastSinged(): Flow<Long>

    fun getData(): Flow<SysTable>

    fun getIsDataPresent(): Flow<Int>

}

class DefaultSystemRepository(
    private val dao : SysTableDao
) : SystemRepository{

    override suspend fun insert(data: SysTable) {
        dao.insert(data)
    }

    override suspend fun update(data: SysTable) {
        dao.update(data)
    }

    override fun getLastUser(): Flow<String> = dao.getLastUser()

    override fun getLanguage(): Flow<String> = dao.getLanguage()

    override fun getLastSinged(): Flow<Long> = dao.getLastSinged()

    override fun getData(): Flow<SysTable> = dao.getSysData()

    override fun getIsDataPresent(): Flow<Int> = dao.getIsDataPresent()

}