package com.mocan.autoreflex.ui.learning

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

    private lateinit var current: List<Map.Entry<String, Boolean>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.practice_fragment, container, false)

        nrQuestion = root.findViewById(R.id.nr_intrebare)
        question = root.findViewById(R.id.intrebarea)
        image = root.findViewById(R.id.image)
        btnList.add(root.findViewById(R.id.A))
        btnList.add(root.findViewById(R.id.B))
        btnList.add(root.findViewById(R.id.C))

        val next = root.findViewById<Button>(R.id.next)

        root.findViewById<Button>(R.id.verifica).setOnClickListener { enable(false)
            for (i in 0 until btnList.size)
                if (current[i].value)
                    btnList[i].setBackgroundColor(Color.RED)
                else
                    btnList[i].setBackgroundColor(Color.GREEN)
            next.isEnabled = true
        }

        next.setOnClickListener { updateUI()
            next.isEnabled = true
            enable(true)
            btnList.forEach { button: MaterialButton -> button.setBackgroundColor(Color.TRANSPARENT) }
        }

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(PracticeViewModel::class.java)
        viewModel.gatherData(category, index)
        viewModel.finished.task.addOnCompleteListener { task ->
            if (task.isSuccessful && task.result == true)
                updateUI()
        }
    }



    private fun updateUI() {
        val question = viewModel.getQuestion()
        if (question == null)
            enable(false)
        else {
            nrQuestion.text = getString(R.string.intrebarea, viewModel.index, viewModel.fullSize)
            this.question.text = question.question
            current = question.answers.entries.shuffled()

            for (i in 0 until btnList.size)
                btnList[i].text = current[i].key
        }
    }

    private fun enable(enable: Boolean) {
        btnList.forEach { materialButton: MaterialButton -> materialButton.isEnabled = enable  }
    }

}
