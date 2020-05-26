package com.example.todoapp.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentTaskBinding
import com.example.todoapp.task.viewmodel.TaskViewModel

class TaskFragment : Fragment() {


    lateinit var viewModelBinding : FragmentTaskBinding

    val viewmodel by viewModels<TaskViewModel>()

override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
): View? {

      viewModelBinding = FragmentTaskBinding.inflate(inflater)

      return  viewModelBinding.root
}

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModelBinding.viewmodel = viewmodel
        viewModelBinding.lifecycleOwner = this.viewLifecycleOwner

    }




}
