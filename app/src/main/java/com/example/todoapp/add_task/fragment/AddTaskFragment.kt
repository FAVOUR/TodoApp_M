package com.example.todoapp.add_task.fragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todoapp.ADD_EDIT_RESULT_OK
import com.example.todoapp.EventObserver

import com.example.todoapp.R
import com.example.todoapp.add_task.viewmodel.AddTaskViewModel
import com.example.todoapp.util.setupRefreshLayout
import com.example.todoapp.util.setupSnackbar
import com.google.android.material.snackbar.Snackbar

class AddTaskFragment : Fragment() {

//    private lateinit var viewDataBinding: AddtaskFragBinding
//
//    private val args: AddEditTaskFragmentArgs by navArgs()
//
//    private val viewModel by viewModels<AddEditTaskViewModel>()
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val root = inflater.inflate(R.layout.addtask_frag, container, false)
//        viewDataBinding = AddtaskFragBinding.bind(root).apply {
//            this.viewmodel = viewModel
//        }
//        // Set the lifecycle owner to the lifecycle of the view
//        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
//        return viewDataBinding.root
//    }
//
//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        setupSnackbar()
//        setupNavigation()
//        this.setupRefreshLayout(viewDataBinding.refreshLayout)
//        viewModel.start(args.taskId)
//    }
//
//    private fun setupSnackbar() {
//        view?.setupSnackbar(this, viewModel.snackbarText, Snackbar.LENGTH_SHORT)
//    }
//
//    private fun setupNavigation() {
//        viewModel.taskUpdatedEvent.observe(this, EventObserver {
//            val action = AddEditTaskFragmentDirections
//                .actionAddEditTaskFragmentToTasksFragment(ADD_EDIT_RESULT_OK)
//            findNavController().navigate(action)
//        })
//    }

}
