package com.example.todoapp.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentTaskBinding

class TaskFragment : Fragment() {


    lateinit var viewModelBinding : FragmentTaskBinding

override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
): View? {

      viewModelBinding = FragmentTaskBinding.inflate(inflater)

      return  viewModelBinding.root
//      inflater.inflate(R.layout.fragment_task,container,false)
}

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

//        viewModelBinding=
    }




}
