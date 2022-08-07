package com.example.yetanothertodolist.Backend

import retrofit2.Response
import retrofit2.http.*

interface YetAnotherAPI {

    @GET("list")
    suspend fun getList(): Response<ServerList>


    @PATCH("list")
    suspend fun updateServerList(
        @Header("X-Last-Known-Revision") number: Long,
        @Body list: ServerList
    ): Response<ServerList>


    @GET("list/{id}")
    suspend fun getElement(@Path("id") id: String): Response<ServerOneElement>


    @POST("list")
    suspend fun addElement(
        @Header("X-Last-Known-Revision") number: Long,
        @Body element: ServerOneElement
    ): Response<ServerOneElement>


    @PUT("list/{id}")
    suspend fun changeElement(
        @Header("X-Last-Known-Revision") number: Long,
        @Path("id") id: String,
        @Body element: ServerOneElement
    ): Response<ServerOneElement>

    @DELETE("list/{id}")
    suspend fun deleteElement(
        @Header("X-Last-Known-Revision") number: Long,
        @Path("id") id: String,
    ): Response<ServerOneElement>

}
