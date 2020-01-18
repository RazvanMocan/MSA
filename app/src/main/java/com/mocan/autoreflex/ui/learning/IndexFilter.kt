package com.mocan.autoreflex.ui.learning

import android.text.InputFilter
import android.text.Spanned
import java.lang.NumberFormatException

class IndexFilter(private val min: Int,private val max: Int): InputFilter {


    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence? {

        if (dest.isNullOrEmpty())
            return source
        try {
            val input = Integer.parseInt(dest.toString().replaceRange(dstart, dend, source.toString().substring(start, end)))

            if (isInRange(input))
                return source
        } catch (e: NumberFormatException) {
            return source
        }

        return ""
    }

    private fun isInRange(c :Int): Boolean {
        return c in min..max
    }
}