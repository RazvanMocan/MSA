package com.mocan.autoreflex.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class HomeViewModel : ViewModel() {

    companion object Admin {
        private val _text = MutableLiveData<List<String>>()
        private val _texts = ArrayList<String>()
        private val _texts2 = ArrayList<String>()

        val text: LiveData<List<String>> = _text
        var dates: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)

        init {
            _text.value = _texts
        }
    }

    fun getData(): LiveData<List<String>> {
        return text
    }

    fun lateCar(t: List<com.mocan.autoreflex.ui.car.dummy.DummyContent.CarItem>) {
        val today = Date()
        _texts.clear()
        for (carItem in t) {
            if(dateDiff(carItem.itp, today) < 15 || dateDiff(carItem.insurance, today) < 15)
                _texts.add(carItem.id)
        }
        addData()
    }

    fun lateInstructor(
        t: Map<String, com.mocan.autoreflex.ui.instructor.dummy.DummyContent.InstructorItem> ) {
        val today = Date()
        _texts2.clear()
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
        val list = ArrayList<String>()
        list.addAll(_texts)
        list.addAll(_texts2)
        _text.postValue(list)
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