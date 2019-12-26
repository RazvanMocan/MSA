package com.mocan.autoreflex.ui.folder

import android.os.Bundle
import android.util.Log
import android.view.Gravity.CENTER
import android.view.Gravity.LEFT
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.mocan.autoreflex.R


class FolderFragment : Fragment() {

    companion object {
        fun newInstance() = FolderFragment()
    }

    private lateinit var viewModel: FolderViewModel
    private lateinit var taskList: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var root = inflater.inflate(R.layout.folder_fragment, container, false)
        taskList = root.findViewById(R.id.documentsList)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FolderViewModel::class.java)

        val task = viewModel.getTasks()


        val checkParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
        )
        checkParams.setMargins(10, 10, 10, 10)
        checkParams.gravity = LEFT

        task.addOnSuccessListener { list ->
            for (document in list) {
                val check = CheckBox(this.context)
                check.text = document
                taskList.addView(check, checkParams)
            }
        }

    }

}
