package com.mocan.autoreflex.ui.car

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
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
    private val oposite = mapOf<Int, Int>(View.GONE to View.VISIBLE, View.VISIBLE to View.GONE)
    private val calendar: Calendar
    private lateinit var myContext: Context

    init {
        mOnClickListener = View.OnClickListener { v ->
            val details = (v as LinearLayout).getChildAt(1)
            details.visibility = oposite.getValue(details.visibility)
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
        holder.mIdView.text = item.index
        holder.mContentView.text = item.id
        holder.mExpandableView.car_itp.text.insert(0, item.itp)
        holder.mExpandableView.car_insurance.text.insert(0, item.insurance)

        holder.mExpandableView.car_itp.setOnClickListener { v ->
            datePicker(v)
        }

        holder.mExpandableView.car_insurance.setOnClickListener { v ->
            datePicker(v)
        }

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)


        }
    }

    private fun datePicker(v: View?) {
        val curDay = calendar.get(Calendar.DAY_OF_MONTH)
        val curMonth = calendar.get(Calendar.MONTH) + 1
        val curYear = calendar.get(Calendar.YEAR)

        val dialog = DatePickerDialog(
            myContext,
            android.R.style.Theme_Material_Dialog_MinWidth,
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                (v as EditText).editableText.clear()
                v.editableText.insert(0, "$dayOfMonth/$month/$year")
            },
            curYear, curMonth, curDay
        )

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
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
