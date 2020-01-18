package com.mocan.autoreflex.ui.learning

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.mocan.autoreflex.R

class PracticeFragment : Fragment() {

    companion object {
        fun newInstance() = PracticeFragment()
    }

    private lateinit var viewModel: PracticeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.practice_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(PracticeViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
