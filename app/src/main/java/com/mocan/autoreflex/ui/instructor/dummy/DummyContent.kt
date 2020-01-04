package com.mocan.autoreflex.ui.instructor.dummy

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
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

    var ITEMS = MutableLiveData<Map<String, InstructorItem>>()


    /**
     * A map of sample (dummy) items, by ID.
     */
    private val ITEM_MAP: MutableMap<String, InstructorItem> = HashMap()


    init {
        val database = FirebaseDatabase.getInstance().reference

        database.child("Instructors").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.e("DummyContent", "cancelled")
            }

            override fun onDataChange(p0: DataSnapshot) {
                ITEM_MAP.clear()
                for (child in p0.children) {
                    val car = child.getValue(InstructorItem::class.java)
                    ITEM_MAP[child.key!!] = car!!
                }
                ITEMS.value = ITEM_MAP
            }
        })
    }


    /**
     * A dummy item representing a piece of content.
     */
    @IgnoreExtraProperties
    data class InstructorItem(
        var atestat: String = "", var buletin: String = "", var permis: String = "",
        var asig: String = "", var cazier: String = "")
}
