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

    @GET("campaigns/{email}")
    suspend fun getCampaigns(@Path("email") email : String) : List<RemoteCampaign>

    @POST("campaign")
    suspend fun createCampaign(@Body campaign: RemoteCampaign): RemoteCampaign

    @DELETE("campaign")
    suspend fun deleteCampaign(@Body campaign: RemoteCampaign): RemoteCampaign

    @PUT("campaign")
    suspend fun updateCampaign(@Body campaign: RemoteCampaign): RemoteCampaign

    //Personajes

    @GET("characters/{campaignId}")
    suspend fun getCharacters(@Path("campaignId") campaignId : String) : List<RemoteCharacter>

    @POST("character")
    suspend fun createCharacter(@Body character: RemoteCharacter): RemoteCharacter

    @DELETE("character")
    suspend fun deleteCharacter(@Body character: RemoteCharacter): RemoteCharacter

    @PUT("character")
    suspend fun updateCharacter(@Body character: RemoteCharacter): RemoteCharacter

    //Criaturas

    @GET("creatures/{campaignId}")
    suspend fun getCreatures(@Path("campaignId") campaignId : String) : List<RemoteCreature>

    @POST("creature")
    suspend fun createCreature(@Body creature: RemoteCreature): RemoteCreature

    @DELETE("creature")
    suspend fun deleteCreature(@Body creature: RemoteCreature): RemoteCreature

    @PUT("creature")
    suspend fun updateCreature(@Body creature: RemoteCreature): RemoteCreature

    //Notas

    @GET("notes/{ownerId}")
    suspend fun getNotes(@Path("ownerId") ownerId : String) : List<RemoteNote>

    @POST("note")
    suspend fun createNote(@Body note: RemoteNote): RemoteNote

    @DELETE("note")
    suspend fun deleteNote(@Body note: RemoteNote): RemoteNote

    @PUT("note")
    suspend fun updateNote(@Body note: RemoteNote): RemoteNote

    //Objetos

    @GET("objects/{campaignId}")
    suspend fun getObjects(@Path("campaignId") campaignId : String) : List<RemoteObject>

    @POST("object")
    suspend fun createObject(@Body obxecto: RemoteObject): RemoteObject

    @DELETE("object")
    suspend fun deleteObject(@Body obxecto: RemoteObject): RemoteObject

    @PUT("object")
    suspend fun updateObject(@Body obxecto: RemoteObject): RemoteObject

    //Lugares

    @GET("places/{campaignId}")
    suspend fun getPlaces(@Path("campaignId") campaignId : String) : List<RemotePlace>

    @POST("place")
    suspend fun createPlace(@Body place: RemotePlace): RemotePlace

    @DELETE("place")
    suspend fun deletePlace(@Body place: RemotePlace): RemotePlace

    @PUT("place")
    suspend fun updatePlace(@Body place: RemotePlace): RemotePlace

    //Usuarios

    @GET("user/{email}")
    suspend fun getUser(@Path("email") email : String) : RemoteUser

    @POST("user")
    suspend fun createUser(@Body remoteUser: RemoteUser): RemoteUser

    @DELETE("user")
    suspend fun deleteUser(@Body user: RemoteUser): RemoteUser

    @PUT("user")
    suspend fun updateUser(@Body user: RemoteUser): RemoteUser

    //Relaciones

    @GET("userRelation/{campaignId}")
    suspend fun getRelations(@Path("campaignId") campaignId: String) : List<RemoteUserRelation>

    @POST("userRelation")
    suspend fun createRelation(@Body relation: RemoteUserRelation): RemoteUserRelation

    @DELETE("userRelation")
    suspend fun deleteRelation(@Body relation: RemoteUserRelation): RemoteUserRelation

    @PUT("userRelation")
    suspend fun updateRelation(@Body relation: RemoteUserRelation): RemoteUserRelation

}