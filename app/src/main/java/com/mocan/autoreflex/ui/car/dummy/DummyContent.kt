package com.mocan.autoreflex.ui.car.dummy

import android.util.Log
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.firebase.database.*
import java.util.ArrayList
import java.util.HashMap

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 *
 */
object DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    private val items: MutableList<CarItem> = ArrayList()

    var ITEMS = TaskCompletionSource<List<CarItem>>()

    /**
     * A map of sample (dummy) items, by ID.
     */
    val ITEM_MAP: MutableMap<String, CarItem> = HashMap()



    private val COUNT = 25

    init {
        val database = FirebaseDatabase.getInstance().reference

        database.child("Cars").addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                ITEMS.setException(p0.toException())
                Log.e("DummyContent", "cancelled")
            }

            override fun onDataChange(p0: DataSnapshot) {
                var i = 1
                Log.e("cars", p0.childrenCount.toString())
                for (child in p0.children) {
                    val car = child.getValue(CarItem::class.java)
                    Log.e("car", car.toString())
                    car?.id = child.key!!
                    car?.index = i.toString()
                    Log.e("car", car.toString())
                    addItem(car!!)
                    i++
                }
                ITEMS.setResult(items)
            }
        })
    }


    private fun addItem(item: CarItem) {
        items.add(item)
        ITEM_MAP[item.id] = item
    }


    /**
     * A data item representing car's documents.
     */
    @IgnoreExtraProperties
    data class CarItem(var id:String = "", var index: String = "", var itp: String = "",
                       var insurance: String = "") {
        override fun toString(): String {
            return id
        }
    }
}
