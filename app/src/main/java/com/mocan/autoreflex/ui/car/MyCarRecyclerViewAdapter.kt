package com.mocan.autoreflex.ui.car

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import android.widget.LinearLayout
import android.widget.TextView
import com.mocan.autoreflex.R


import com.mocan.autoreflex.ui.car.CarFragment.OnListFragmentInteractionListener
import com.mocan.autoreflex.ui.car.dummy.DummyContent.CarItem

import kotlinx.android.synthetic.main.fragment_car.view.*

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

    init {
        mOnClickListener = View.OnClickListener { v ->
            val details = (v as LinearLayout).getChildAt(1)
            details.visibility = oposite.getValue(details.visibility)
            val item = v.tag as CarItem
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_car, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
//        Log.w("id elem", holder.mIdView)
        holder.mIdView.text = item.index
        holder.mContentView.text = item.id
        holder.mExpandableView.content2.text = item.insurance

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
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
