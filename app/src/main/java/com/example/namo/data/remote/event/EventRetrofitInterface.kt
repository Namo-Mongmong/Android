package com.example.namo.data.remote.event

import com.example.namo.data.entity.home.EventForUpload
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface EventRetrofitInterface {
    @POST("schedules")
    fun postEvent(
//        @Header("Authorization") token : String,
        @Body event : EventForUpload
    ) : Call<PostEventResponse>

    @PATCH("schedules/{serverIdx}")
    fun editEvent(
        @Path("serverIdx") serverIdx : Int,
//        @Header("Authorization") token : String,
        @Body event : EventForUpload
    ) : Call<EditEventResponse>

    @DELETE("schedules/{serverIdx}")
    fun deleteEvent(
        @Path("serverIdx") serverIdx : Int,
//        @Header("Authorization") token : String,
    ) : Call<DeleteEventResponse>
}