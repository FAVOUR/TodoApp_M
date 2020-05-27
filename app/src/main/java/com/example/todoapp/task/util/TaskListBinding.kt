package com.example.todoapp.task.util

import android.graphics.Paint
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.data.Task
import com.example.todoapp.task.TaskAdapter

class TaskListBinding {

    @BindingAdapter("items")
    fun bindItems(view: RecyclerView, itemList :List<Task>){
        itemList?.let {
            (view.adapter as TaskAdapter).submitList(itemList)
        }
    }

    @BindingAdapter("app:completedTask")
    fun setStyle(textView: TextView, enabled: Boolean) {
        if (enabled) {
            textView.paintFlags = textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            textView.paintFlags = textView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
    }
}