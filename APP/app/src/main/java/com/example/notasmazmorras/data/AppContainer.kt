package com.example.notasmazmorras.data

import android.content.Context
import com.example.notasmazmorras.data.repositories.CampaignRepository
import com.example.notasmazmorras.data.repositories.CharacterRepository
import com.example.notasmazmorras.data.repositories.CreatureRepository
import com.example.notasmazmorras.data.repositories.DefaultCampaignRepository
import com.example.notasmazmorras.data.repositories.DefaultCharacterRepository
import com.example.notasmazmorras.data.repositories.DefaultCreatureRepository
import com.example.notasmazmorras.data.repositories.DefaultNoteRepository
import com.example.notasmazmorras.data.repositories.DefaultObjectRepository
import com.example.notasmazmorras.data.repositories.DefaultPlaceRepository
import com.example.notasmazmorras.data.repositories.DefaultUserRelationRepository
import com.example.notasmazmorras.data.repositories.DefaultUserRepository
import com.example.notasmazmorras.data.repositories.NoteRepository
import com.example.notasmazmorras.data.repositories.ObjectRepository
import com.example.notasmazmorras.data.repositories.PlaceRepository
import com.example.notasmazmorras.data.repositories.UserRelationRepository
import com.example.notasmazmorras.data.repositories.UserRepository
import com.example.notasmazmorras.network.ApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

interface AppContainer {
    val campaignRepository : CampaignRepository
    val characterRepository : CharacterRepository
    val creatureRepository : CreatureRepository
    val noteRepository : NoteRepository
    val objectRepository : ObjectRepository
    val placeRepository : PlaceRepository
    val userRelationRepository : UserRelationRepository
    val userRepository : UserRepository
}

class AppDataContainer(private val context : Context) : AppContainer {

    private val urlAPI : String = "LINK API"

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    var interceptor : HttpLoggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    var client : OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .client(client)
        .baseUrl(urlAPI).build()

    private val retrofitService : ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    override val campaignRepository : CampaignRepository by lazy {
        DefaultCampaignRepository(Datosbase.getDatabase(context).campaignDao(), retrofitService)
    }

    override val characterRepository : CharacterRepository by lazy {
        DefaultCharacterRepository(Datosbase.getDatabase(context).characterDao(), retrofitService)
    }

    override val creatureRepository : CreatureRepository by lazy {
        DefaultCreatureRepository(Datosbase.getDatabase(context).creatureDao(), retrofitService)
    }

    override val noteRepository : NoteRepository by lazy {
        DefaultNoteRepository(Datosbase.getDatabase(context).noteDao(), retrofitService)
    }

    override val objectRepository : ObjectRepository by lazy {
        DefaultObjectRepository(Datosbase.getDatabase(context).objectDao(), retrofitService)
    }

    override val placeRepository : PlaceRepository by lazy {
        DefaultPlaceRepository(Datosbase.getDatabase(context).placeDao(), retrofitService)
    }

    override val userRepository : UserRepository by lazy {
        DefaultUserRepository(Datosbase.getDatabase(context).userDao(), retrofitService)
    }

    override val userRelationRepository : UserRelationRepository by lazy {
        DefaultUserRelationRepository(Datosbase.getDatabase(context).userRelationDao(), retrofitService)
    }

}