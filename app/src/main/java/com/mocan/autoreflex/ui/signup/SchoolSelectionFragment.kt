package com.mocan.autoreflex.ui.signup

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mocan.autoreflex.R


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [SchoolSelectionFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [SchoolSelectionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SchoolSelectionFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null
    private val optionsList: ArrayList<String> = ArrayList()
    private val typesList: ArrayList<String> = ArrayList()

    private lateinit var progress: ProgressBar
    private lateinit var label: TextView
    private lateinit var select: Button
    private lateinit var type: Button

    private var school: String = ""
    private var category: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_school_selection, container, false)

        select = root.findViewById(R.id.select_school)
        type = root.findViewById(R.id.select_type)
        progress = root.findViewById(R.id.options_progress)
        label = root.findViewById(R.id.options_label)

        select.isEnabled = false
        getOptions()

        select.setOnClickListener { v -> showOptions() }

        type.setOnClickListener { v -> showTypes() }

        return root
    }

    private fun getTypeOptions() {
        val database = FirebaseDatabase.getInstance().reference
        val ref = database.child("Schools").child(school).child("Category")
        typesList.clear()

        val phoneQuery = ref.orderByKey()
        phoneQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (singleSnapshot in dataSnapshot.children) {
                    typesList.add(singleSnapshot.value!! as String)
                }
                next()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("database", "onCancelled", databaseError.toException())
            }
        })
    }

    private fun next() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun getOptions() {
        val database = FirebaseDatabase.getInstance().reference
        val ref = database.child("Schools")

        val phoneQuery = ref.orderByKey()
        phoneQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (singleSnapshot in dataSnapshot.children) {
                    optionsList.add(singleSnapshot.key!!)
                }
                finishWait()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("database", "onCancelled", databaseError.toException())
            }
        })
    }

    private fun updateDB(id: String) {
        val database = FirebaseDatabase.getInstance().reference
        val ref = database.child("Schools").child(school).child("Students").
                child(id).setValue(category)
    }

    private fun finishWait() {
        progress.visibility = View.GONE
        label.visibility = View.GONE

        select.isEnabled = true
    }

    fun showOptions() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Choose an animal")

        builder.setSingleChoiceItems(optionsList.toTypedArray(), 0 , DialogInterface.OnClickListener
        { dialog, which ->
                school = optionsList.get(which)
                getTypeOptions()
                type.isEnabled = true
                dialog.dismiss()
        })
        // create and show the alert dialog
        val dialog = builder.create()
        dialog.show()
    }

    fun showTypes() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Choose an animal")

        builder.setSingleChoiceItems(typesList.toTypedArray(), 0 , DialogInterface.OnClickListener
        { dialog, which ->
            category = typesList.get(which)
            updateDB(FirebaseAuth.getInstance().uid!!)
            next()
            dialog.dismiss()
            onButtonPressed(2)
        })
        // create and show the alert dialog
        val dialog = builder.create()
     }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Int) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
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
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Int)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SchoolSelectionFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SchoolSelectionFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
