package com.mongmong.namo.presentation.ui.home.schedule

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mongmong.namo.domain.model.GetMonthScheduleResult
import com.mongmong.namo.domain.model.group.AddMoimScheduleRequestBody
import com.mongmong.namo.domain.model.group.EditMoimScheduleRequestBody
import com.mongmong.namo.domain.model.group.MoimScheduleBody
import com.mongmong.namo.domain.repositories.ScheduleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoimScheduleViewModel @Inject constructor(
    private val repository: ScheduleRepository
) : ViewModel() {
    private val _schedule = MutableLiveData<GetMonthScheduleResult>()
    val schedule: LiveData<GetMonthScheduleResult> = _schedule

    private val _groupScheduleList = MutableLiveData<List<MoimScheduleBody>>(emptyList())
    val groupScheduleList: LiveData<List<MoimScheduleBody>?> = _groupScheduleList

    /** 그룹의 모든 일정 조회 */
    fun getGroupAllSchedules(groupId: Long) {
        viewModelScope.launch {
            Log.d("MoimScheduleViewModel", "getGroupAllSchedules")
            _groupScheduleList.value = repository.getGroupAllSchedules(groupId)
        }
    }

    /** 모임 일정 생성 */
    fun postMoimSchedule(moimSchedule: AddMoimScheduleRequestBody) {
        viewModelScope.launch {
            repository.addMoimSchedule(moimSchedule = moimSchedule)
        }
    }

    /** 모임 일정 수정 */
    fun editMoimSchedule(moimSchedule: EditMoimScheduleRequestBody) {
        viewModelScope.launch {
            repository.editMoimSchedule(moimSchedule = moimSchedule)
        }
    }

    /** 모임 일정 삭제 */
    fun deleteMoimSchedule(moimScheduleId: Long) {
        viewModelScope.launch {
            repository.deleteMoimSchedule(moimScheduleId)
        }
    }
}