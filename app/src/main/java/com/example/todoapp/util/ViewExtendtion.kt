package com.example.todoapp.util

import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.todoapp.Event
import com.example.todoapp.R
import com.example.todoapp.ScrollChildSwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson

//class ViewExtendtion {


    /**
     * Transforms static java function Snackbar.make() to an extension function on View.
     */
    fun View.showSnackbar(snackbarText: String, timeLength: Int) {
        Snackbar.make(this, snackbarText, timeLength).run {
            show()
        }
    }

    /**
     * Triggers a snackbar message when the value contained by snackbarTaskMessageLiveEvent is modified.
     */
    fun View.setupSnackbar(
        lifecycleOwner: LifecycleOwner,
        snackbarEvent: LiveData<Event<Int>>,
        timeLength: Int
    ) {

        snackbarEvent.observe(lifecycleOwner, Observer { event ->
            event.getContentIfNotHandled()?.let {
                Log.i("dummy  setupSnackbar(....) ViewExtendtion", Gson().toJson( it))
                Log.i("dummy  setupSnackbar(....)  showSnackbar(context.getString(it), timeLength) ViewExtendtion",context.getString(it))

                showSnackbar(context.getString(it), timeLength)
            }
        })
    }

    fun Fragment.setupRefreshLayout(
        refreshLayout: ScrollChildSwipeRefreshLayout,
        scrollUpChild: View? = null
    ) {
        refreshLayout.setColorSchemeColors(
            ContextCompat.getColor(requireActivity(), R.color.colorPrimary),
            ContextCompat.getColor(requireActivity(), R.color.colorAccent),
            ContextCompat.getColor(requireActivity(), R.color.colorPrimaryDark)
        )
        // Set the scrolling view in the custom SwipeRefreshLayout.
        scrollUpChild?.let {
            refreshLayout.scrollUpChild = it
        }
    }
//}