package com.example.notasmazmorras.network

import com.example.notasmazmorras.data.model.remote.RemoteCampaign
import com.example.notasmazmorras.data.model.remote.RemoteCharacter
import com.example.notasmazmorras.data.model.remote.RemoteCreature
import com.example.notasmazmorras.data.model.remote.RemoteNote
import com.example.notasmazmorras.data.model.remote.RemoteObject
import com.example.notasmazmorras.data.model.remote.RemotePlace
import com.example.notasmazmorras.data.model.remote.RemoteUser
import com.example.notasmazmorras.data.model.remote.RemoteUserRelation
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    //Campañas

    @GET("campaigns")
    suspend fun getCampaigns() : List<RemoteCampaign>

    @POST("campaigns")
    suspend fun createCampaign(@Body campaign: RemoteCampaign): RemoteCampaign

    @DELETE("campaigns/{id}")
    suspend fun deleteCampaign(@Path("id") id: String): RemoteCampaign

    @PUT("campaigns/{id}")
    suspend fun updateCampaign(@Path("id") id : String, @Body campaign: RemoteCampaign): RemoteCampaign

    //Personajes

    @GET("characters")
    suspend fun getCharacters() : List<RemoteCharacter>

    @POST("characters")
    suspend fun createCharacter(@Body character: RemoteCharacter): RemoteCharacter

    @DELETE("characters/{id}")
    suspend fun deleteCharacter(@Path("id") id: String): RemoteCharacter

    @PUT("characters/{id}")
    suspend fun updateCharacter(@Path("id") id : String, @Body character: RemoteCharacter): RemoteCharacter

    //Criaturas

    @GET("creatures")
    suspend fun getCreatures() : List<RemoteCreature>

    @POST("creatures")
    suspend fun createCreature(@Body creature: RemoteCreature): RemoteCreature

    @DELETE("creatures/{id}")
    suspend fun deleteCreature(@Path("id") id: String): RemoteCreature

    @PUT("creatures/{id}")
    suspend fun updateCreature(@Path("id") id : String, @Body creature: RemoteCreature): RemoteCreature

    //Notas

    @GET("notes")
    suspend fun getNotes() : List<RemoteNote>

    @POST("notes")
    suspend fun createNote(@Body note: RemoteNote): RemoteNote

    @DELETE("notes/{id}")
    suspend fun deleteNote(@Path("id") id: String): RemoteNote

    @PUT("notes/{id}")
    suspend fun updateNote(@Path("id") id : String, @Body note: RemoteNote): RemoteNote

    //Objetos

    @GET("objects")
    suspend fun getObjects() : List<RemoteObject>

    @POST("objects")
    suspend fun createObject(@Body obxecto: RemoteObject): RemoteObject

    @DELETE("objects/{id}")
    suspend fun deleteObject(@Path("id") id: String): RemoteObject

    @PUT("objects/{id}")
    suspend fun updateObject(@Path("id") id : String, @Body obxecto: RemoteObject): RemoteObject

    //Lugares

    @GET("places")
    suspend fun getPlaces() : List<RemotePlace>

    @POST("places")
    suspend fun createPlace(@Body place: RemotePlace): RemotePlace

    @DELETE("places/{id}")
    suspend fun deletePlace(@Path("id") id: String): RemotePlace

    @PUT("places/{id}")
    suspend fun updatePlace(@Path("id") id : String, @Body place: RemotePlace): RemotePlace

    //Usuarios

    @GET("users")
    suspend fun getUsers() : List<RemoteUser>

    @POST("users")
    suspend fun createUser(@Body remoteUser: RemoteUser): RemoteUser

    @DELETE("users/{id}")
    suspend fun deleteUser(@Path("id") id: String): RemoteUser

    @PUT("users/{id}")
    suspend fun updateUser(@Path("id") id : String, @Body user: RemoteUser): RemoteUser

    //Relaciones

    @GET("relations")
    suspend fun getRelations() : List<RemoteUserRelation>

    @POST("relations")
    suspend fun createRelation(@Body relation: RemoteUserRelation): RemoteUserRelation

    @DELETE("relations/{id}")
    suspend fun deleteRelation(@Path("id") id: String): RemoteUserRelation

    @PUT("relations/{id}")
    suspend fun updateRelation(@Path("id") id : String, @Body relation: RemoteUserRelation): RemoteUserRelation

}