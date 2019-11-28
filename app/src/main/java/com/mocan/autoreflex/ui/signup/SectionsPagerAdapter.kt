package com.mocan.autoreflex.ui.signup

import android.content.Context
import android.net.Uri
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.mocan.autoreflex.R

private val TAB_TITLES = arrayOf(
    R.string.tab_1_username,
    R.string.tab_2_id,
    R.string.tab_3_school
)

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return when (position) {
            0 -> UserCreationFragment.newInstance("", "")
            1 -> SchoolSelectionFragment.newInstance("", "")
            else -> IDFragment.newInstance("", "")
        }

    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        // Show 2 total pages.
        return 3
    }
}