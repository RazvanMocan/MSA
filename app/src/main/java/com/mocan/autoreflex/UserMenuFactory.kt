package com.mocan.autoreflex

import com.google.android.material.navigation.NavigationView

class UserMenuFactory(private var type: String) {

    fun selectView(navView: NavigationView): Set<Int> {
        if (type != "scoala") {
            navView.menu.findItem(R.id.nav_home).isVisible = true
            navView.menu.findItem(R.id.nav_gallery).isVisible = true
            navView.menu.findItem(R.id.nav_tasks).isVisible = true
            navView.menu.findItem(R.id.car_fragment).isVisible = true

            return setOf(
                R.id.nav_home,
                R.id.nav_gallery,
                R.id.nav_tasks,
                R.id.car_fragment
            )
        } else {
            navView.menu.findItem(R.id.nav_home).isVisible = true
            navView.menu.findItem(R.id.nav_slideshow).isVisible = true
            navView.menu.findItem(R.id.nav_tools).isVisible = true
            navView.menu.findItem(R.id.nav_share).isVisible = true
            navView.menu.findItem(R.id.nav_send).isVisible = true
            navView.menu.findItem(R.id.cominicate).isVisible = true
            navView.menu.findItem(R.id.documentsList).isVisible = true
        }

        return setOf(
            R.id.nav_home,
            R.id.nav_slideshow,
            R.id.nav_tools,
            R.id.nav_share,
            R.id.nav_send,
            R.id.documentsList
        )
    }
}