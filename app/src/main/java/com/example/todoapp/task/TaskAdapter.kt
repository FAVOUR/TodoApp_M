package com.example.todoapp.task

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.data.Task
import com.example.todoapp.databinding.TaskItemBinding
import com.example.todoapp.task.viewmodel.TaskViewModel

class TaskAdapter(private val viewModel :TaskViewModel):ListAdapter<Task,TaskRv>(TaskDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskRv {
      return  TaskRv.from(parent)
    }

    override fun onBindViewHolder(holder: TaskRv, position: Int) {
        val item = getItem(position)
        holder.bind(viewModel, item)
    }

}

class TaskRv(val binding: TaskItemBinding): RecyclerView.ViewHolder(binding.root){

    fun bind(viewModel: TaskViewModel, item: Task) {

        binding.viewmodel = viewModel
        binding.task = item
        binding.executePendingBindings()
    }


    companion object {
        fun from(parent: ViewGroup): TaskRv {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = TaskItemBinding.inflate(layoutInflater, parent, false)

            return TaskRv(binding)
        }
    }
}

 class TaskDiffUtil : DiffUtil.ItemCallback<Task>(){
     override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
         return oldItem.id == newItem.id
     }

     override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem == newItem

     }
 }


