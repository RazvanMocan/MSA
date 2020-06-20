package com.mocan.autoreflex.ui.learning

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.firebase.database.*
import java.net.URL
import java.util.concurrent.TimeUnit


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

                for (child in p0.children) {
                    val question = child.getValue(TheoryQuestion::class.java)
                    list.add(question!!)
                }

                for (i in 1..index)
                    if (list.isNotEmpty())
                        list.removeAt(0)

                getImage(true, 2)
            }
        })
    }

    fun getQuestion(): TheoryQuestion? {
        Thread {getImage(false, 10)}.start()
        if (list.isEmpty())
            return null
        index++
        return list.removeAt(0)
    }

    private fun getImage(first: Boolean, br:Int) {
        var i = 1
        for (question in list) {
            val task = DownloadFilesTask()
            if (question.picture.isNotEmpty() && question.photo == null) {
                question.photo = task.execute(URL(question.picture)).get(1, TimeUnit.SECONDS)[0]
            }

            if (i == br)
                break
            i++
        }

        if (first)
            finished.setResult(true)
    }


    @IgnoreExtraProperties
    data class TheoryQuestion(var question: String = "", var picture: String = "",
                              var answers: Map<String, Boolean> = emptyMap()) {
        var photo: Bitmap? = null
    }


}
