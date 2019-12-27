package com.mocan.autoreflex.ui.folder

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.firebase.database.*


class FolderViewModel(private var database:DatabaseReference = FirebaseDatabase.getInstance().reference) : ViewModel() {
    private val category = "Tasks"
    private lateinit var taskList: ArrayList<String>

    companion object Admin {
        var admin = false
    }

    fun admin(): Boolean {
        Log.e("admin", admin.toString())
        return admin
    }

    fun setAdmin(str: String?) {
        Log.e("admin", "set")
        admin = str != "scoala"
        Log.e("admin", admin.toString())

    }

    fun getTasks(): Task<List<String>> {
        val listener = TaskCompletionSource<List<String>>()

        taskList = ArrayList()

        val ref = database.child(category)

        ref.orderByKey().addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.e("database", "onCancelled", p0.toException())
                listener.setException(p0.toException())
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (task in p0.children) {
                    taskList.add(task.value.toString())
                }

                listener.setResult(taskList)
            }
        })

        return listener.task
    }

    fun addTask(task: String) {
        val key = taskList.size + 1

        val users: HashMap<String, String> = HashMap()
        users[key.toString()] = task

        database.child(category).updateChildren(users as Map<String, Any>)
    }
}
