package com.mongmong.namo.domain.model

import com.mongmong.namo.presentation.config.BaseResponse
import com.google.gson.annotations.SerializedName
import com.mongmong.namo.data.local.entity.home.Schedule
import com.mongmong.namo.presentation.config.RoomState
import com.mongmong.namo.presentation.config.UploadState

// 개인
/** 일정 월별 조회 */
data class GetMonthScheduleResponse (
    @SerializedName("result") val result : List<GetMonthScheduleResult>
) : BaseResponse()

data class GetMonthScheduleResult (
    @SerializedName("scheduleId") val scheduleId : Long,
    @SerializedName("name") val name : String,
    @SerializedName("startDate") val startDate : Long,
    @SerializedName("endDate") val endDate : Long,
    @SerializedName("alarmDate") val alarmDate : List<Int>,
    @SerializedName("interval") val interval : Int,
    @SerializedName("x") val x : Double,
    @SerializedName("y") val y : Double,
    @SerializedName("locationName") val locationName : String,
    @SerializedName("categoryId") val categoryId : Long,
    @SerializedName("hasDiary") val hasDiary : Boolean?,
    @SerializedName("moimSchedule") val moimSchedule : Boolean,
) {
    fun convertServerScheduleResponseToLocal(): Schedule {
        return Schedule(
            this.scheduleId, // localId
            this.name,
            this.startDate,
            this.endDate,
            this.interval,
            this.categoryId,
            this.locationName,
            this.x,
            this.y,
            0,
            this.alarmDate ?: listOf(),
            UploadState.IS_UPLOAD.state,
            RoomState.DEFAULT.state,
            this.scheduleId,
            this.categoryId,
            this.hasDiary,
            this.moimSchedule
        )
    }
}

/** 일정 생성 */
data class PostScheduleResponse (
    val result : PostScheduleResult
) : BaseResponse()

data class PostScheduleResult (
    @SerializedName("scheduleId") val scheduleId : Long
)

data class ScheduleRequestBody(
    var name: String = "",
    var startDate: Long = 0L,
    var endDate: Long = 0L,
    var interval: Int = 0,
    var alarmDate: List<Int>? = listOf(),
    var x: Double = 0.0,
    var y: Double = 0.0,
    var locationName: String = "없음",
    var categoryId: Long = 0L
)

/** 일정 수정 */
data class EditScheduleResponse (
    @SerializedName("result") val result : EditScheduleResult
) : BaseResponse()

data class EditScheduleResult (
    @SerializedName("scheduleId")  val scheduleId : Long
)

/** 일정 삭제 */
data class DeleteScheduleResponse (
    @SerializedName("result") val result : String
) : BaseResponse()

// 모임
/** 모임 일정 카테고리 수정 */
data class PatchMoimScheduleCategoryRequestBody(
    @SerializedName("moimScheduleId") val moimScheduleId: Long,
    @SerializedName("categoryId") val categoryId : Long
)

/** 모임 일정 알림 리스트 수정 */
data class PatchMoimScheduleAlarmRequestBody(
    @SerializedName("moimScheduleId") val moimScheduleId: Long,
    @SerializedName("alarmDates") val alarmDates : List<Int>
)