package com.example.yetanothertodolist.data.sources

import com.example.yetanothertodolist.data.model.ServerList
import com.example.yetanothertodolist.data.model.ServerOneElement
import retrofit2.Response
import retrofit2.http.*

/**
 * Класс для работы с сетью
 */
interface YetAnotherAPI {

    @GET("list")
    suspend fun getList(): Response<ServerList>


    @PATCH("list")
    suspend fun updateList(
        @Header("X-Last-Known-Revision") number: Long,
        @Body list: ServerList
    ): Response<ServerList>

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
