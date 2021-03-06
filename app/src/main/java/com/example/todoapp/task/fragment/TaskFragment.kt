package com.example.todoapp.task.fragment

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todoapp.EventObserver
import com.example.todoapp.R
import com.example.todoapp.ServiceLocator
import com.example.todoapp.databinding.FragmentTaskBinding
import com.example.todoapp.task.TaskAdapter
import com.example.todoapp.task.util.TasksFilterType
import com.example.todoapp.task.viewmodel.TaskViewModel
import com.example.todoapp.ViewModelFactory
import com.example.todoapp.util.setupRefreshLayout
import com.example.todoapp.util.setupSnackbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber

class TaskFragment : Fragment() {


    private val viewModel by viewModels<TaskViewModel>{
        ViewModelFactory(
            ServiceLocator.provideRepository(
                this.requireContext()
            )
        )
    }

    private val args: TaskFragmentArgs by navArgs()

    private lateinit var viewDataBinding: FragmentTaskBinding

    private lateinit var listAdapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = FragmentTaskBinding.inflate(inflater, container, false).apply {
          viewmodel = viewModel
        }

        setHasOptionsMenu(true)
        return viewDataBinding.root
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.menu_clear -> {
                viewModel.clearCompletedTasks()
                true
            }
            R.id.menu_filter -> {
                showFilteringPopUpMenu()
                true
            }
            R.id.menu_refresh -> {
                viewModel.loadTasks(true)
                true
            }
            else -> false
        }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.tasks_fragment_menu, menu)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Set the lifecycle owner to the lifecycle of the view
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        setupSnackbar()
        setupListAdapter()
        setupRefreshLayout(viewDataBinding.refreshLayout, viewDataBinding.tasksList)
        setupNavigation()
        setupFab()
    }

    private fun setupNavigation() {
        viewModel.openTaskEvent.observe(this, EventObserver {
            openTaskDetails(it)
        })
        viewModel.newTaskEvent.observe(this, EventObserver {
            navigateToAddNewTask()
        })
    }

    private fun setupSnackbar() {
        view?.setupSnackbar(this, viewModel.snackbarText, Snackbar.LENGTH_SHORT)
        arguments?.let {
            viewModel.showEditResultMessage(args.userMessage)
        }
    }

    private fun showFilteringPopUpMenu() {
        val view = activity?.findViewById<View>(R.id.menu_filter) ?: return
        PopupMenu(requireContext(), view).run {
            menuInflater.inflate(R.menu.filter_tasks, menu)

            setOnMenuItemClickListener {
                viewModel.setFiltering(
                    when (it.itemId) {
                        R.id.active -> TasksFilterType.ACTIVE_TASKS
                        R.id.completed -> TasksFilterType.COMPLETED_TASKS
                        else -> TasksFilterType.ALL_TASKS
                    }
                )
                true
            }
            show()
        }
    }

    private fun setupFab() {
        activity?.findViewById<FloatingActionButton>(R.id.add_task_fab)?.let {
            it.setOnClickListener {
                navigateToAddNewTask()
            }
        }
    }

    private fun navigateToAddNewTask() {
        val action = TaskFragmentDirections
            .actionTasksFragmentToAddEditTaskFragment(
                null,
                resources.getString(R.string.add_task)
            )
        findNavController().navigate(action)
    }

    private fun openTaskDetails(taskId: String) {
        val action = TaskFragmentDirections.actionTasksFragmentToTaskDetailFragment(taskId)
        findNavController().navigate(action)
    }

    private fun setupListAdapter() {
        val viewModel = viewDataBinding.viewmodel
        if (viewModel != null) {
            listAdapter = TaskAdapter(viewModel)
            viewDataBinding.tasksList.adapter = listAdapter
        } else {
            Timber.w("ViewModel not initialized when attempting to set up adapter.")
        }
    }


}
