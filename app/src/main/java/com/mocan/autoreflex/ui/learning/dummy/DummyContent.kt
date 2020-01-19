package com.mocan.autoreflex.ui.learning.dummy

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mocan.autoreflex.ui.instructor.dummy.DummyContent
import java.util.ArrayList
import java.util.HashMap

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 *
 * TODO: Replace all uses of this class before publishing your app.
 */
object DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    val ITEMS = MutableLiveData<List<String>>()
    private val ITEMS2: MutableList<String> = ArrayList()
    private val COUNT: MutableList<Long> = ArrayList()

    fun getCount(int: Int): Long {
        return COUNT[int]
    }

    init {
        val database = FirebaseDatabase.getInstance().reference

        database.child("categories").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.e("DummyContent", "cancelled")
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (child in p0.children) {
                    val category = child.key

                    COUNT.add(child.childrenCount)
                    ITEMS2.add(category!!)
                }
                ITEMS.value = ITEMS2
            }
        })
    }
}
