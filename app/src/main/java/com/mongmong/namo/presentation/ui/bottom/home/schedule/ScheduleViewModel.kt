package com.mongmong.namo.presentation.ui.bottom.home.schedule

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mongmong.namo.data.local.entity.home.Event
import com.mongmong.namo.domain.repositories.ScheduleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val repository: ScheduleRepository
) : ViewModel() {
    private val _schedule = MutableLiveData<Event>()
    val schedule: LiveData<Event> = _schedule

    fun addSchedule(schedule: Event) {
        viewModelScope.launch {
            Log.d("ScheduleViewModel addSchedule", "$schedule")
            repository.addSchedule(
                schedule = schedule
            )
        }
    }
}