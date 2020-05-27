package com.example.todoapp.edit_task.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels

import com.example.todoapp.databinding.TaskDetailsFragmentBinding
import com.example.todoapp.edit_task.viewmodel.TaskDetailViewModel

class TaskDetailFragment : Fragment() {

    companion object {
        fun newInstance() = TaskDetailFragment()
    }

    private  val viewModel by viewModels<TaskDetailViewModel>()

    private var viewBinding :TaskDetailsFragmentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewBinding = TaskDetailsFragmentBinding.inflate(inflater)

        return viewBinding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

}
