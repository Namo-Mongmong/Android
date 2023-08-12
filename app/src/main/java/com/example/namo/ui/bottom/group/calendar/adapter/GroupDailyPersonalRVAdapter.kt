package com.example.namo.ui.bottom.group.calendar.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.namo.data.entity.home.Event
import com.example.namo.R
import com.example.namo.data.entity.home.Category
import com.example.namo.data.remote.moim.MoimSchedule
import com.example.namo.databinding.ItemCalendarEventBinding
import com.example.namo.databinding.ItemCalendarEventGroupBinding
import org.joda.time.DateTime
import java.lang.Boolean.FALSE
import java.lang.Boolean.TRUE

class GroupDailyPersonalRVAdapter() : RecyclerView.Adapter<GroupDailyPersonalRVAdapter.ViewHolder>() {

    private val personal = ArrayList<MoimSchedule>()
    lateinit var colorArray: IntArray
    private lateinit var context : Context

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType : Int) : ViewHolder {
        val binding : ItemCalendarEventGroupBinding = ItemCalendarEventGroupBinding.inflate(
            LayoutInflater.from(viewGroup.context), viewGroup, false)
        context = viewGroup.context
        colorArray = context.resources.getIntArray(R.array.categoryColorArr)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder : ViewHolder, position : Int) {
        holder.bind(personal[position])
    }

    override fun getItemCount(): Int = personal.size

    @SuppressLint("NotifyDataSetChanged")
    fun addPersonal(personal : ArrayList<MoimSchedule>) {
        this.personal.clear()
        this.personal.addAll(personal)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding : ItemCalendarEventGroupBinding) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("ResourceType")
        fun bind(personal: MoimSchedule) {
            val time =
                DateTime(personal.startDate * 1000L).toString("HH:mm") + " - " + DateTime(personal.endDate * 1000L).toString(
                    "HH:mm"
                )
            val paletteId =
                if (personal.users.size < 2 && personal.users[0].paletteId != 0) personal.users[0].paletteId
                else 3

            binding.itemCalendarEventTitle.text = personal.name
            binding.itemCalendarEventTitle.isSelected = true
            binding.itemCalendarEventTime.text = time
            binding.itemCalendarEventColorView.background.setTint(colorArray[paletteId])
            binding.itemCalendarUserName.text = personal.users[0].userName
        }
    }

}