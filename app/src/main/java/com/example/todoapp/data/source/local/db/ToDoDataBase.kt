//package com.example.todoapp.data.source.local.db
//
//import android.content.Context
//import androidx.room.Database
//import androidx.room.Room
//import androidx.room.RoomDatabase
//import com.example.todoapp.data.Task
//
//@Database(entities = [Task::class],version = 1, exportSchema = false)
//abstract class ToDoDataBase : RoomDatabase() {
//
//   abstract  fun taskDoa() :TaskDao
//
//    companion object{
//      @Volatile
//      private lateinit var  INSTANCE : ToDoDataBase
//        @Synchronized
//           fun setupDatabase(context: Context){
//               if(INSTANCE == null){
//                   INSTANCE = Room.databaseBuilder(context,ToDoDataBase::class.java,"tododb")
//                                  .fallbackToDestructiveMigration()
//                                  .build()
//               }
//           }
//       }
//}