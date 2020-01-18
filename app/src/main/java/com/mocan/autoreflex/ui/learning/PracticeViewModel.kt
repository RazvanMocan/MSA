package com.mocan.autoreflex.ui.learning

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.firebase.database.*

class PracticeViewModel : ViewModel() {
    private val list = ArrayList<TheoryQuestion>()
    val finished = TaskCompletionSource<Boolean>()
    var index = 0
    var fullSize = 0L

    fun gatherData(category: String, index: Int) {
        val database = FirebaseDatabase.getInstance().reference
        database.child("categories").child(category).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.e("DummyContent", "cancelled")
            }

            override fun onDataChange(p0: DataSnapshot) {
                fullSize = p0.childrenCount

                Log.e("lista", fullSize.toString())

                for (child in p0.children) {
                    val question = child.getValue(TheoryQuestion::class.java)
                    list.add(question!!)
                }
                Log.e("index", index.toString())

                for (i in 1..index)
                    if (list.isNotEmpty())
                        list.removeAt(0)

                Log.e("lista", list.size.toString())
                finished.setResult(true)
            }
        })
    }

    fun getQuestion(): TheoryQuestion? {
        if (list.isEmpty())
            return null
        index++
        return list.removeAt(0)
    }


    @IgnoreExtraProperties
    data class TheoryQuestion(var question: String = "", var picture: String = "",
                              var answers: Map<String, Boolean> = emptyMap())
}
