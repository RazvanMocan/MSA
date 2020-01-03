package com.mocan.autoreflex.ui.car

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.mocan.autoreflex.R
import com.mocan.autoreflex.ui.car.CarFragment.OnListFragmentInteractionListener
import com.mocan.autoreflex.ui.car.dummy.DummyContent.CarItem
import kotlinx.android.synthetic.main.fragment_car.view.*
import java.util.*


/**
 * [RecyclerView.Adapter] that can display a [CarItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class MyCarRecyclerViewAdapter(
    private val mValues: List<CarItem>,
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<MyCarRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener
    private val opposite = mapOf(View.GONE to View.VISIBLE, View.VISIBLE to View.GONE)
    private val calendar: Calendar
    private lateinit var myContext: Context
    private var database: DatabaseReference = FirebaseDatabase.getInstance().reference

    init {
        mOnClickListener = View.OnClickListener { v ->
            val details = (v as LinearLayout).getChildAt(1)
            details.visibility = opposite.getValue(details.visibility)
            val item = v.tag as CarItem
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListFragmentInteraction(item)
        }
        calendar = Calendar.getInstance()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_car, parent, false)

        myContext = parent.context

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
//        Log.w("id elem", holder.mIdView)
        holder.mIdView.text = position.inc().toString()
        holder.mContentView.text = item.id
        holder.mExpandableView.car_itp.text.insert(0, item.itp)
        holder.mExpandableView.car_insurance.text.insert(0, item.insurance)

        holder.mExpandableView.car_itp.setOnClickListener { v ->
            val task = datePicker(v)
            task.task.addOnSuccessListener { s ->
                item.itp = s
            }
        }

        holder.mExpandableView.car_insurance.setOnClickListener { v ->
            val task = datePicker(v)
            task.task.addOnSuccessListener { s ->
                item.insurance = s
            }
        }

        holder.mExpandableView.car_update.setOnClickListener {
            database.child("Cars").child(item.id).setValue(item)
        }

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)


        }
    }

    private fun datePicker(v: View?): TaskCompletionSource<String> {
        val curDay = calendar.get(Calendar.DAY_OF_MONTH)
        val curMonth = calendar.get(Calendar.MONTH)
        val curYear = calendar.get(Calendar.YEAR)
        val task = TaskCompletionSource<String>()


        val dialog = DatePickerDialog(
            myContext,
            android.R.style.Theme_Material_Dialog_MinWidth,
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                (v as EditText).editableText.clear()
                val date = "$dayOfMonth/${month.inc()}/$year"
                v.editableText.insert(0, date)
                task.setResult(date)
            },
            curYear, curMonth, curDay
        )

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        return task
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mIdView: TextView = mView.item_number
        val mContentView: TextView = mView.content
        val mExpandableView: LinearLayout = mView.expandView

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }
    }
}
