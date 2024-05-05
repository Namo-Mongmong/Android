package com.mongmong.namo.data.remote.group

import com.mongmong.namo.domain.model.DiaryResponse
import com.mongmong.namo.presentation.config.BaseResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.DELETE
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface GroupDiaryApiService {
    // 모임 기록 활동 추가
    @Multipart
    @POST("group/diaries/{activityId}")
    suspend fun addMoimDiary(
        @Path("activityId") scheduleId: Long,
        @Part("name") place: RequestBody?,
        @Part("money") pay: RequestBody?,
        @Part("participants") member: RequestBody?,
        @Part imgs: List<MultipartBody.Part>?
    ): DiaryResponse

    // 모임 기록 활동 수정
    @Multipart
    @PATCH("group/diaries/{activityId}")
    suspend fun editMoimActivity(
        @Path("activityId") moimScheduldId: Long,
        @Part("name") place: RequestBody?,
        @Part("money") pay: RequestBody?,
        @Part("participants") member: RequestBody?,
        @Part imgs: List<MultipartBody.Part>?
    ): DiaryResponse

    // 모임 기록 활동 삭제
    @DELETE("group/diaries/{activityId}")
    suspend fun deleteMoimActivity(
        @Path("activityId") moimActivityId: Long
    ): DiaryResponse

    // 모임 기록 삭제 (그룹에서 삭제)
    @DELETE("group/diaries/all/{moimDiaryId}")
    suspend fun deleteMoimDiary(
        @Path("moimDiaryId") moimDiaryId: Long
    ): BaseResponse

}