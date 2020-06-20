package com.mocan.autoreflex.ui.learning


import android.annotation.SuppressLint
import androidx.appcompat.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.text.InputType
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mocan.autoreflex.R
import com.mocan.autoreflex.ui.learning.CategoryFragment.OnListFragmentInteractionListener
import com.mocan.autoreflex.ui.learning.dummy.DummyContent
import kotlinx.android.synthetic.main.fragment_learning.view.*


class MyCategoryRecyclerViewAdapter(
    private val mValues: List<String>,
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<MyCategoryRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener
    private lateinit var pref: SharedPreferences
    private lateinit var context: Context


    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Map<String, Int>

            for (entry in item) {
                clickCategory(entry.key, entry.value)

            }
        }
    }

    private fun clickCategory(item: String, count: Int) {
        val llPadding = 30
        val ll = LinearLayout(context)
        ll.orientation = LinearLayout.VERTICAL
        ll.setPadding(llPadding, llPadding, llPadding, llPadding)
        ll.gravity = Gravity.CENTER
        var llParam = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        llParam.gravity = Gravity.CENTER
        ll.layoutParams = llParam

        val view = TextView(context)
        view.setPadding(0, 0, llPadding, 0)
        view.text = context.getString(R.string.press_start)
        view.layoutParams = llParam

        // Get index
        llParam = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        llParam.gravity = Gravity.CENTER

        val index = EditText(context)
        index.inputType = InputType.TYPE_CLASS_NUMBER
        index.filters = Array(1) { IndexFilter(1, DummyContent.getCount(count).toInt())}
        index.hint = context.getString(R.string.jump_ahead, DummyContent.getCount(count))
        index.textSize = 20f
        index.layoutParams = llParam
        ll.addView(view)
        ll.addView(index)

        val builder = AlertDialog.Builder(context)
        builder.setTitle("Start learning")
        builder.setView(ll)

        builder.setPositiveButton("Start"
        ) { _, _ ->
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            var i: Int = if (index.text.toString().isEmpty())
                pref.getInt(item,0)
            else
                index.text.toString().toInt() - 1
            if (i == DummyContent.getCount(count).toInt())
                i = 0

            mListener?.onListFragmentInteraction(item, i)
        }

        builder.setNegativeButton("Cancel"
        ) { _, _ ->

        }

        // create and show the alert dialog
        val dialog = builder.create()
        dialog.show()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_learning, parent, false)
        pref = parent.context!!.getSharedPreferences("categories", Context.MODE_PRIVATE)
        context = parent.context

        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mIdView.max = mValues.size - 1
        val prog = pref.getInt(item, 0)
        holder.mIdView.progress = prog
        holder.mIdView.secondaryProgress = mValues.size

        holder.mPercentView.text = "${prog.toFloat().div(DummyContent.getCount(position)).times(100).toInt()}%"

        holder.mContentView.text = item

        with(holder.mView) {
            val tmp = HashMap<String, Int>()
            tmp[item] = position
            tag = tmp
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mIdView: ProgressBar = mView.progress
        val mPercentView: TextView = mView.item_percentage
        val mContentView: TextView = mView.content

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }
    }
}
