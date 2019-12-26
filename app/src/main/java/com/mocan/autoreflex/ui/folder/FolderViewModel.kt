package com.mocan.autoreflex.ui.folder

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.firebase.database.*

class FolderViewModel(private var database:DatabaseReference = FirebaseDatabase.getInstance().reference) : ViewModel() {
    private val category = "Tasks"

    fun getTasks(): Task<List<String>> {
        val listener = TaskCompletionSource<List<String>>()

        Log.e("eroare aici", "aici")
        val taskList = ArrayList<String>()

        val ref = database.child(category)

        ref.orderByKey().addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.e("database", "onCancelled", p0.toException())
                listener.setException(p0.toException())
            }

            override fun onDataChange(p0: DataSnapshot) {
                Log.e("eroare aici", p0.hasChildren().toString())
                for (task in p0.children) {
                    taskList.add(task.value.toString())
                }

                listener.setResult(taskList)
            }
        })
        Log.w("eroare aici", taskList.toString())

        return listener.task
    }
}
