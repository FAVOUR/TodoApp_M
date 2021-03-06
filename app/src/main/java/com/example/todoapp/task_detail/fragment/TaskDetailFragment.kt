package com.example.todoapp.task_detail.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todoapp.*
import com.example.todoapp.databinding.TaskDetailsFragmentBinding
import com.example.todoapp.task_detail.viewmodel.TaskDetailViewModel
import com.example.todoapp.util.setupRefreshLayout
import com.example.todoapp.util.setupSnackbar
import com.google.android.material.snackbar.Snackbar

class TaskDetailFragment : Fragment() {

    private lateinit var viewDataBinding: TaskDetailsFragmentBinding

    private val args: TaskDetailFragmentArgs by navArgs()

    private val viewModel by viewModels<TaskDetailViewModel>{
        ViewModelFactory((requireContext().applicationContext as TodoApplication).taskRepository!!)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupFab()
        view?.setupSnackbar(this, viewModel.snackbarText, Snackbar.LENGTH_SHORT)
        setupNavigation()
        this.setupRefreshLayout(viewDataBinding.refreshLayout)
    }

    private fun setupNavigation() {
        viewModel.deleteTaskEvent.observe(this, EventObserver {
            val action = TaskDetailFragmentDirections
                .actionTaskDetailFragmentToTasksFragment(DELETE_RESULT_OK)
            findNavController().navigate(action)
        })

        viewModel.editTaskEvent.observe(this, EventObserver {
            val action = TaskDetailFragmentDirections.actionTaskDetailFragmentToAddEditTaskFragment(
                args.taskId, resources.getString(R.string.edit_task))
            findNavController().navigate(action)
        })
    }

    private fun setupFab() {
        activity?.findViewById<View>(R.id.edit_task_fab)?.setOnClickListener {
            viewModel.editTask()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.task_details_fragment, container, false)
        viewDataBinding = TaskDetailsFragmentBinding.bind(view).apply {
            viewmodel = viewModel
        }
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner

        Log.i("dummy args.taskId TaskDetailsFragment",args.taskId)
        viewModel.start(args.taskId)

        setHasOptionsMenu(true)
        return view
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_delete -> {
                viewModel.deleteTask()
                true
            }
            else -> false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.taskdetail_fragment_menu, menu)
    }
}
