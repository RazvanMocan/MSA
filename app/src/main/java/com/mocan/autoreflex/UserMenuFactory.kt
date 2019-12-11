package com.mocan.autoreflex

import com.google.android.material.navigation.NavigationView

class UserMenuFactory {
    private var type:String

    constructor(type: String) {
        this.type = type
    }

    fun selectView(navView: NavigationView): Set<Int> {

        return setOf(
            R.id.nav_home,
            R.id.nav_gallery,
            R.id.nav_slideshow,
            R.id.nav_tools,
            R.id.nav_share,
            R.id.nav_send
        )
    }
}