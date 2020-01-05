package com.mocan.autoreflex.ui.folder

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.Gravity.LEFT
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.mocan.autoreflex.R


class FolderFragment : Fragment() {

    companion object {
        fun newInstance() = FolderFragment()
    }

    val checkParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
    )

    private lateinit var viewModel: FolderViewModel
    private lateinit var taskList: LinearLayout
    private lateinit var taskButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        checkParams.setMargins(10, 10, 10, 10)
        checkParams.gravity = LEFT
        val root = inflater.inflate(R.layout.folder_fragment, container, false)
        taskList = root.findViewById(R.id.documentsList)
        taskButton = root.findViewById(R.id.add_task)
        
        taskButton.setOnClickListener { v -> showDialog() }
        
        return root
    }

    private fun showDialog() {
        val alert = AlertDialog.Builder(this.context)

        alert.setTitle("Task")
        alert.setMessage("Input new task")

        // Set an EditText view to get user input
        val input = EditText(this.context)
        alert.setView(input)

        alert.setPositiveButton("Ok",
            DialogInterface.OnClickListener { dialog, whichButton ->
                val task = input.text.toString()
                viewModel.addTask(task)

                val check = CheckBox(this.context)
                check.text = task
                taskList.addView(check, checkParams)

                // Do something with value!
            })

        alert.setNegativeButton("Cancel",
            DialogInterface.OnClickListener { dialog, whichButton ->
                // Canceled.
            })

        alert.show()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FolderViewModel::class.java)

        if (viewModel.admin())
            taskButton.visibility = View.VISIBLE

        val task = viewModel.getTasks()


        task.addOnSuccessListener { list ->
            for (document in list) {
                val check = CheckBox(this.context)
                check.text = document
                taskList.addView(check, checkParams)
            }
        }

    }

}
