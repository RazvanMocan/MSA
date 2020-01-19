package com.mocan.autoreflex.ui.learning

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.graphics.Color
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.card.MaterialCardView

import com.mocan.autoreflex.R


class PracticeFragment : Fragment() {

    companion object {
        var category = ""
        var index = 0
    }

    private lateinit var viewModel: PracticeViewModel
    private lateinit var nrQuestion: TextView
    private lateinit var question: TextView
    private lateinit var image: ImageView
    private val btnList = ArrayList<MaterialButton>()
    private lateinit var pref:SharedPreferences
    private lateinit var group: MaterialButtonToggleGroup
    private lateinit var end: MaterialCardView


    private lateinit var current: List<Map.Entry<String, Boolean>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.practice_fragment, container, false)
        pref = this.context!!.getSharedPreferences("categories", MODE_PRIVATE)


        nrQuestion = root.findViewById(R.id.nr_intrebare)
        question = root.findViewById(R.id.intrebarea)
        image = root.findViewById(R.id.image)
        btnList.add(root.findViewById(R.id.A))
        btnList.add(root.findViewById(R.id.B))
        btnList.add(root.findViewById(R.id.C))
        group = root.findViewById(R.id.toggle_button_group)
        end = root.findViewById(R.id.end)

        val next = root.findViewById<Button>(R.id.next)

        root.findViewById<Button>(R.id.verifica).setOnClickListener { enable(false)
            for (i in 0 until btnList.size)
                if (current[i].value)
                    btnList[i].setBackgroundColor(Color.GREEN)
                else
                    btnList[i].setBackgroundColor(Color.RED)
            next.isEnabled = true
            val editor = pref.edit()
            editor.putInt(category, viewModel.index)
            editor.apply()
        }

        next.setOnClickListener { updateUI()
            next.isEnabled = true
            enable(true)
            btnList.forEach { button: MaterialButton -> button.setBackgroundColor(Color.TRANSPARENT) }
            group.clearChecked()
        }


        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(PracticeViewModel::class.java)
        viewModel.index = index
        viewModel.gatherData(category, index)
        viewModel.finished.task.addOnCompleteListener { task ->
            if (task.isSuccessful && task.result == true)
                updateUI()
        }
    }



    private fun updateUI() {
        val question = viewModel.getQuestion()
        if (question == null) {
            enable(false)
            end.visibility = View.VISIBLE
        }
        else {
            nrQuestion.text = getString(R.string.intrebarea, viewModel.index, viewModel.fullSize)
            this.question.text = question.question
            current = question.answers.entries.shuffled()
            image.setImageBitmap(question.photo)


            for (i in 0 until btnList.size)
                btnList[i].text = current[i].key
        }
    }

    private fun enable(enable: Boolean) {
        btnList.forEach { materialButton: MaterialButton -> materialButton.isEnabled = enable  }
    }

}
