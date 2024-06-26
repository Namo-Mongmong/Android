package com.mongmong.namo.presentation.ui.group.diary.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mongmong.namo.databinding.ItemDiaryGroupEventBinding
import com.mongmong.namo.domain.model.group.MoimActivity
import java.text.NumberFormat
import java.util.*


class MoimActivityRVAdapter(
    // 그룹 다이어리 장소 추가, 정산, 이미지
    val context: Context,
    private val listData: MutableList<MoimActivity>,
    val payClickListener: (pay: Long, position: Int, payText: TextView) -> Unit,
    val imageClickListener: (imgLists: List<String>?, position: Int) -> Unit,
    val activityClickListener: (text: String, position: Int) -> Unit,
    val deleteItemList: (deleteItems: MutableList<Long>) -> Unit
) : RecyclerView.Adapter<MoimActivityRVAdapter.Holder>() {

    private val items = arrayListOf<ArrayList<String>?>()
    private var deleteItems = arrayListOf<Long>()

    @SuppressLint("NotifyDataSetChanged")
    fun addImageItem(image: ArrayList<String>?) {
        this.items.add(image)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            ItemDiaryGroupEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    @SuppressLint("NotifyDataSetChanged", "ClickableViewAccessibility")
    override fun onBindViewHolder(holder: Holder, position: Int) {

        val event = listData[position]
        val updatedPosition = holder.bindingAdapterPosition

        with(holder.binding) {
            // 정산 다이얼로그
            itemPlaceMoneyTv.text =
                NumberFormat.getNumberInstance(Locale.US).format(event.pay)
            clickMoneyLy.setOnClickListener {
                payClickListener(event.pay, updatedPosition, holder.binding.itemPlaceMoneyTv)
            }

            // 장소별 이미지 가져오기
            val adapter = MoimActivityGalleryAdapter(context)
            groupAddGalleryRv.apply {
                this.adapter = adapter.apply {
                    itemClickListener = if (event.imgs?.size == 3) {
                        {
                            imageClickListener(event.imgs, updatedPosition)
                            Log.d("dd","dd")
                        }
                    } else {
                        null
                    }
                }
                layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }
            if (event.imgs?.size == 3) {
                img1.visibility = View.GONE
                img2.visibility = View.GONE
                img3.visibility = View.GONE
            } else if (event.imgs?.isNotEmpty() == true) {
                img1.visibility = View.VISIBLE
                img2.visibility = View.GONE
                img3.visibility = View.GONE
            } else {
                img1.visibility = View.VISIBLE
                img2.visibility = View.VISIBLE
                img3.visibility = View.VISIBLE
            }

            holder.binding.groupGalleryLv.setOnClickListener {
                imageClickListener(event.imgs, updatedPosition)
            }

            holder.binding.itemPlaceNameTv.hint = "활동"
            event.imgs?.let { adapter.addItem(it) }
        }

        holder.bind(event)

    }

    override fun getItemCount(): Int = listData.size


    inner class Holder(val binding: ItemDiaryGroupEventBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("NotifyDataSetChanged")
        fun bind(item: MoimActivity) {

            binding.itemPlaceNameTv.setText(item.place)

            binding.itemPlaceNameTv.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(p0: Editable?) {
                    activityClickListener(p0.toString(), absoluteAdapterPosition)
                }
            })

            binding.groupLayout.translationX = 0f

            binding.onclickDeleteItem.setOnClickListener {
                deleteItems.add(item.moimActivityId)
                deleteItemList(deleteItems)
                listData.remove(item)
                notifyDataSetChanged()
            }
        }
    }
}
