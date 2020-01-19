package com.mocan.autoreflex.ui.home

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.marginTop
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.mocan.autoreflex.R


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        val lateDocs: LinearLayout = root.findViewById(R.id.late_docs)

        textView.text = "Welcome ${homeViewModel.userName}!"


        homeViewModel.text.observe(this, Observer {

            it.forEachIndexed { index, s ->
                if (index == homeViewModel.cars - 1) {
                    val view = textView("Instructors with documents that will expire:")
                    view.gravity = Gravity.START
                    lateDocs.addView(view)
                } else if (index == 0) {
                    val cars = textView("Cars with documents that will expire:")
                    cars.gravity = Gravity.START
                    lateDocs.addView(cars)
                    textView.visibility = View.GONE
                }

                val view = textView(s)
                view.gravity = Gravity.END
                lateDocs.addView(view)
            }

        })
        return root
    }

    private fun textView(s: String): TextView {
        val view = TextView(this.context)
        view.textSize = 20F
        view.text = s
        return view
    }
}