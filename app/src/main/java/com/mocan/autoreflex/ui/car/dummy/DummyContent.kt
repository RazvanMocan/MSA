package com.mocan.autoreflex.ui.car.dummy

import android.util.Log
import androidx.lifecycle.MutableLiveData
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

    var ITEMS = MutableLiveData<List<CarItem>>()

    /**
     * A map of sample (dummy) items, by ID.
     */
    private val ITEM_MAP: MutableMap<String, CarItem> = HashMap()


    init {
        val database = FirebaseDatabase.getInstance().reference


        database.child("Cars").addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                Log.e("DummyContent", "cancelled")
            }

            override fun onDataChange(p0: DataSnapshot) {
                items.clear()
                for (child in p0.children) {
                    val car = child.getValue(CarItem::class.java)
                    car?.id = child.key!!
                    addItem(car!!)
                }
                ITEMS.value = items
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
    data class CarItem(var itp: String = "",
                       var insurance: String = "") {
        var id:String = ""

        override fun toString(): String {
            return id
        }
    }
}
