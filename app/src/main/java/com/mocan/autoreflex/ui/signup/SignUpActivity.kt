package com.mocan.autoreflex.ui.signup

import android.net.Uri
import android.os.Bundle

import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.mocan.autoreflex.R
import com.mocan.autoreflex.ui.login.LoginViewModel
import com.mocan.autoreflex.ui.login.LoginViewModelFactory

class SignUpActivity : AppCompatActivity(),
                       UserCreationFragment.OnFragmentInteractionListener,
                        IDFragment.OnFragmentInteractionListener,
                        SchoolSelectionFragment.OnFragmentInteractionListener{
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        val sectionsPagerAdapter =
            SectionsPagerAdapter(this, supportFragmentManager)
        viewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter

        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

        loginViewModel = ViewModelProviders.of(this,
            LoginViewModelFactory()
        )
            .get(LoginViewModel::class.java)
    }

    override fun onFragmentInteraction(int: Int) {
        viewPager.currentItem = int
    }

    override fun onFragmentInteraction(uri: Uri) {
        finish()
    }
}