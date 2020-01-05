package com.mocan.autoreflex.ui.instructor

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.mocan.autoreflex.R
import com.mocan.autoreflex.ui.instructor.dummy.DummyContent


import com.mocan.autoreflex.ui.instructor.instructorFragment.OnListFragmentInteractionListener
import com.mocan.autoreflex.ui.instructor.dummy.DummyContent.InstructorItem

import kotlinx.android.synthetic.main.fragment_instructor.view.*
import kotlinx.android.synthetic.main.fragment_instructor.view.content
import kotlinx.android.synthetic.main.fragment_instructor.view.item_number
import java.util.*
import kotlin.collections.ArrayList

/**
 * [RecyclerView.Adapter] that can display a [InstructorItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class MyinstructorRecyclerViewAdapter(
    private val mValues: Map<String, InstructorItem>,
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<MyinstructorRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener
    private val keyList: List<String>
    private val opposite = mapOf(View.GONE to View.VISIBLE, View.VISIBLE to View.GONE)
    private val calendar: Calendar
    private lateinit var myContext: Context
    private var database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val tab = "Instructors"


    init {
        mOnClickListener = View.OnClickListener { v ->
            val details = (v as LinearLayout).getChildAt(1)
            details.visibility = opposite.getValue(details.visibility)
            val item = v.tag as InstructorItem
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListFragmentInteraction(item)
        }
        keyList = ArrayList(mValues.keys)
        calendar = Calendar.getInstance()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_instructor, parent, false)


        myContext = parent.context

        return ViewHolder(view)
    }




    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.e("nu tare", holder.mExpandableView.toString())
        Log.e("nu tare", mValues.toString())

        val item = mValues.getValue(keyList[position])
        holder.mIdView.text = position.inc().toString()
        holder.mContentView.text = keyList[position]

        holder.mExpandableView.instructor_atestat.text.insert(0, item.atestat)
        holder.mExpandableView.instructor_buletin.text.insert(0, item.buletin)
        holder.mExpandableView.instructor_cazier.text.insert(0, item.cazier)
        holder.mExpandableView.instructor_medical.text.insert(0, item.asig)
        holder.mExpandableView.instructor_permis.text.insert(0, item.permis)


        holder.mExpandableView.instructor_atestat.setOnClickListener { v ->
            val task = datePicker(v)
            task.task.addOnSuccessListener { s ->
                item.atestat = s

            }
        }

        holder.mExpandableView.instructor_buletin.setOnClickListener { v ->
            val task = datePicker(v)
            task.task.addOnSuccessListener { s ->
                item.buletin = s
            }
        }


        holder.mExpandableView.instructor_permis.setOnClickListener { v ->
            val task = datePicker(v)
            task.task.addOnSuccessListener { s ->
                item.permis = s
            }
        }


        holder.mExpandableView.instructor_medical.setOnClickListener { v ->
            val task = datePicker(v)
            task.task.addOnSuccessListener { s ->
                item.asig = s
            }
        }


        holder.mExpandableView.instructor_cazier.setOnClickListener { v ->
            val task = datePicker(v)
            task.task.addOnSuccessListener { s ->
                item.cazier = s
            }
        }

        holder.mExpandableView.instructor_update.setOnClickListener {
            database.child(tab).child(keyList[position]).setValue(item)
        }

        holder.mDeleteInstr.setOnClickListener {
            database.child(tab).child(keyList[position]).removeValue()
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
        val mDeleteInstr: ImageButton = mView.delete_instr
        val mExpandableView: LinearLayout = mView.expandViewInstr


        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }
    }
}
