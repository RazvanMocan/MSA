package com.mocan.autoreflex.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.mocan.autoreflex.ui.instructor.dummy.DummyContent
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<List<String>>()
    private val _texts = ArrayList<String>()
    private val _texts2 = ArrayList<String>()
    private val database:DatabaseReference
    private var nr = 0
    var cars = 0
    val userName: String

    val text: LiveData<List<String>> = _text
    var dates: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)

    init {
        _text.value = _texts
        database = FirebaseDatabase.getInstance().reference
        userName = FirebaseAuth.getInstance().currentUser?.displayName.toString()

        database.child("Instructors").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.e("DummyContent", "cancelled")
            }

            override fun onDataChange(p0: DataSnapshot) {
                val map = HashMap<String, DummyContent.InstructorItem>()
                for (child in p0.children) {
                    val car = child.getValue(DummyContent.InstructorItem::class.java)
                    map[child.key!!] = car!!
                }
                lateInstructor(map)
            }
        })

        database.child("Cars").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.e("DummyContent", "cancelled")
            }

            override fun onDataChange(p0: DataSnapshot) {
                val map = HashMap<String, com.mocan.autoreflex.ui.car.dummy.DummyContent.CarItem>()
                for (child in p0.children) {
                    val car = child.getValue(com.mocan.autoreflex.ui.car.dummy.DummyContent.CarItem::class.java)
                    map[child.key!!] = car!!
                }
                lateCar(map)
            }
        })

    }


    fun lateCar(t: Map<String, com.mocan.autoreflex.ui.car.dummy.DummyContent.CarItem>) {
        val today = Date()
        cars = t.size
        for (car in t) {
            val carItem = car.value
            if(dateDiff(carItem.itp, today) < 15 || dateDiff(carItem.insurance, today) < 15)
                _texts.add(car.key)
        }
        addData()
    }

    fun lateInstructor(
        t: Map<String, DummyContent.InstructorItem> ) {
        val today = Date()
        for (instructorItem in t) {
            val carItem = instructorItem.value
            if(dateDiff(carItem.asig, today) < 15 || dateDiff(carItem.atestat, today) < 15 ||
                dateDiff(carItem.buletin, today) < 15 || dateDiff(carItem.cazier, today) < 15 ||
                dateDiff(carItem.permis, today) < 15)
                _texts2.add(instructorItem.key)
        }
        addData()
    }

    private fun addData() {
        if (nr == 0) {
            nr++
            return
        }
        _texts.addAll(_texts2)
        _texts2.clear()
        _text.postValue(_texts)
    }


    private fun dateDiff(
        carItem: String,
        today: Date
    ): Long {
        val date1 = try {
            dates.parse(carItem)

        } catch (e: Exception) {
            return 16
        }

        val difference: Long = today.time - date1.time
        return difference / (24 * 60 * 60 * 1000)
    }
}