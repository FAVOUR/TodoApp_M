package com.example.todoapp

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var navController:NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        setSupportActionBar(findViewById(R.id.toolbar))

        navController = Navigation.findNavController(this,R.id.nav_host)
        nav_view.setupWithNavController(navController)
        appBarConfiguration =
            AppBarConfiguration.Builder(R.id.tasks_fragment_dest)
                .setDrawerLayout(nav_drawer)
                .build()

//        NavigationUI.setupActionBarWithNavController(this,navController,nav_drawer)
        setupActionBarWithNavController(navController,appBarConfiguration)
    }


    override fun onSupportNavigateUp(): Boolean {

       return  if(nav_drawer.isDrawerOpen(GravityCompat.START)){
            nav_drawer.closeDrawer(GravityCompat.START)
            true
        }else {
             findNavController(R.id.nav_host).navigateUp(appBarConfiguration)
                   || super.onSupportNavigateUp()
       }
    }

}

// Keys for navigation
const val ADD_EDIT_RESULT_OK = Activity.RESULT_FIRST_USER + 1
const val DELETE_RESULT_OK = Activity.RESULT_FIRST_USER + 2
const val EDIT_RESULT_OK = Activity.RESULT_FIRST_USER + 3

