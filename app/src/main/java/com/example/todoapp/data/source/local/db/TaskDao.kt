package com.example.todoapp.data.source.local.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.todoapp.data.Task


/**
 * Data Access Object for the tasks table.
 */

@Dao
interface TaskDao {
    /**
     * Observes list of tasks.
     *
     * @return all tasks.
     */

       @Query("Select * from Task")
         fun observeTasks():LiveData<List<Task>>


    /**
     * Observes a single task.
     *
     * @param taskId the task id.
     * @return the task with taskId.
     */
    @Query("Select * from Task where entryid = :taskId")
    fun observeTaskById(taskId:String):LiveData<Task>



    /**
     * Select all tasks from the tasks table.
     *
     * @return all tasks.
     */
      @Query("Select * from Task")
     suspend fun getTask(): List<Task>


    /**
     * Select a task by id.
     *
     * @param taskId the task id.
     * @return the task with taskId.
     */
      @Query("Select * from Task where entryid =:taskId ")
      suspend fun  getTaskById(taskId: String):Task?


    /**
     * Insert a task in the database. If the task already exists, replace it.
     *
     * @param task the task to be inserted.
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE )
      suspend  fun insertTask(task:Task)

    /**
     * Update a task.
     *
     * @param task task to be updated
     * @return the number of tasks updated. This should always be 1.
     */

    @Update
    suspend  fun updateTask(task:Task):Int


/**
 * Update the complete status of a task
 *
 * @param taskId    id of the task
 * @param completed status to be updated
 */
    @Query("Update task Set completed =:completed  where  entryId = :taskId")
    suspend fun updateCompleted(taskId: String, completed: Boolean)


    /**
     * Delete a task by id.
     *
     * @return the number of tasks deleted. This should always be 1.
     */

    @Query("Delete From Task where entryid =:taskId")
    suspend fun  deleteTaskById(taskId:String)


    /**
     * Delete all tasks.
     */


   @Query("Delete from Task")
    suspend fun  deleteTask()



    /**
     * Delete all completed tasks from the table.
     *
     * @return the number of tasks deleted.
     */
    @Query("Delete from Task where completed = 1")
    suspend fun deleteCompletedTask()



}