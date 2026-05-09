package com.example.notasmazmorras.data.repositories

import android.graphics.Bitmap
import android.util.Log
import com.example.notasmazmorras.data.model.local.SysTable
import com.example.notasmazmorras.data.model.remote.Suggestion
import com.example.notasmazmorras.data.repositories.daos.SysTableDao
import com.example.notasmazmorras.network.ApiService
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream

interface SystemRepository {

    suspend fun insert(data : SysTable)

    suspend fun update(data : SysTable)

    suspend fun sendReport(report: Suggestion)

    fun getLastUser(): Flow<String>

    fun getLanguage(): Flow<String>

    fun getLastSinged(): Flow<Long>

    fun getData(): Flow<SysTable>

    fun getIsDataPresent(): Flow<Int>

    suspend fun uploadImage(image: Bitmap)

}

class DefaultSystemRepository(
    private val dao : SysTableDao,
    private val remote : ApiService
) : SystemRepository{

    override suspend fun insert(data: SysTable) {
        dao.insert(data)
    }

    override suspend fun update(data: SysTable) {
        dao.update(data)
    }

    override suspend fun sendReport(report: Suggestion) {
        remote.sendReport(report)
    }

    override fun getLastUser(): Flow<String> = dao.getLastUser()

    override fun getLanguage(): Flow<String> = dao.getLanguage()

    override fun getLastSinged(): Flow<Long> = dao.getLastSinged()

    override fun getData(): Flow<SysTable> = dao.getSysData()

    override fun getIsDataPresent(): Flow<Int> = dao.getIsDataPresent()

    override suspend fun uploadImage(image: Bitmap) {
        val stream = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 80, stream)

        val byteArray = stream.toByteArray()
        val body = MultipartBody.Part.createFormData(
            "image", "photo${System.nanoTime()}",
            byteArray.toRequestBody("image/jpeg".toMediaTypeOrNull(), 0, byteArray.size)
        )

        try{
            remote.uploadImage(body)
        }catch(e: Throwable){
            Log.d("ERROR_IMG", "${e.message}")
        }

    }

}