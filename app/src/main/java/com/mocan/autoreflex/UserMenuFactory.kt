package com.mocan.autoreflex

import com.google.android.material.navigation.NavigationView

class UserMenuFactory {
    private var type:String

    constructor(type: String) {
        this.type = type
    }

    fun selectView(navView: NavigationView): Set<Int> {
        if (!type.equals("scoala")) {
            navView.menu.findItem(R.id.nav_home).isVisible = true
            navView.menu.findItem(R.id.nav_gallery).isVisible = true

            return setOf(
                R.id.nav_home,
                R.id.nav_gallery
            )
        } else {
            navView.menu.findItem(R.id.nav_home).isVisible = true
            navView.menu.findItem(R.id.nav_slideshow).isVisible = true
            navView.menu.findItem(R.id.nav_tools).isVisible = true
            navView.menu.findItem(R.id.nav_share).isVisible = true
            navView.menu.findItem(R.id.nav_send).isVisible = true
            navView.menu.findItem(R.id.cominicate).isVisible = true
        }

        return setOf(
            R.id.nav_home,
            R.id.nav_slideshow,
            R.id.nav_tools,
            R.id.nav_share,
            R.id.nav_send
        )
    }
}