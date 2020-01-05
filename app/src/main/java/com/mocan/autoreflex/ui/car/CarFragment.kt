package com.mocan.autoreflex.ui.car

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import androidx.core.view.get
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.mocan.autoreflex.R

import com.mocan.autoreflex.ui.car.dummy.DummyContent
import com.mocan.autoreflex.ui.car.dummy.DummyContent.CarItem
import com.mocan.autoreflex.ui.home.HomeViewModel

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [CarFragment.OnListFragmentInteractionListener] interface.
 */
class CarFragment : Fragment() {

    // TODO: Customize parameters
    private var columnCount = 1

    private var listener: OnListFragmentInteractionListener? = null
    private lateinit var homeViewModel: HomeViewModel
    private var database: DatabaseReference = FirebaseDatabase.getInstance().reference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_car_list, container, false)
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)

        val recycler = (view as RelativeLayout).get(0)

        val button = view.findViewById<Button>(R.id.add_instr)
        button.setOnClickListener { v ->
            showDialog(v.context)
        }

        // Set the adapter
        if (recycler is RecyclerView) {
            with(recycler) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                DummyContent.ITEMS.observeForever { t ->
                    adapter = MyCarRecyclerViewAdapter(t, listener)
                }

            }
        }

        return view
    }

    private fun showDialog(context: Context) {
        val alert = AlertDialog.Builder(context)

        alert.setTitle("Task")
        alert.setMessage("Input new task")

        // Set an EditText view to get user input
        val input = EditText(context)
        alert.setView(input)

        alert.setPositiveButton("Ok"
        ) { _, _ ->
            val instr = input.text.toString()
            database.child("Cars").child(instr).setValue(DummyContent.CarItem())
        }

        alert.setNegativeButton("Cancel"
        ) { _, _ ->
            // Canceled.
        }

        alert.show()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onListFragmentInteraction(item: CarItem?)
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            CarFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}
