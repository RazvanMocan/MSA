package com.mocan.autoreflex

import com.google.android.material.navigation.NavigationView

class UserMenuFactory(private var type: String) {

    fun selectView(navView: NavigationView): Set<Int> {
        if (type != "scoala") {
            navView.menu.findItem(R.id.nav_tasks).isVisible = true
            navView.menu.findItem(R.id.nav_gallery).isVisible = true
            navView.menu.findItem(R.id.law_fragment).isVisible = true
            navView.menu.findItem(R.id.learning_fragment).isVisible = true

            return setOf(
                R.id.nav_tasks,
                R.id.nav_gallery,
                R.id.law_fragment,
                R.id.learning_fragment
                )
        } else {
            navView.menu.findItem(R.id.nav_home).isVisible = true
            navView.menu.findItem(R.id.nav_send).isVisible = true
            navView.menu.findItem(R.id.nav_tasks).isVisible = true
            navView.menu.findItem(R.id.car_fragment).isVisible = true
            navView.menu.findItem(R.id.instructor_fragment).isVisible = true
        }

        return setOf(
            R.id.nav_home,
            R.id.nav_send,
            R.id.documentsList,
            R.id.nav_tasks,
            R.id.car_fragment,
            R.id.instructor_fragment

        )
    }
}