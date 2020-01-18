package com.mocan.autoreflex.ui.law

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.mocan.autoreflex.R

/**
 * A simple [Fragment] subclass.
 */
class LawFragment : Fragment() {
    private var url:String = "https://www.scoalarutiera.ro/curs-legislatie"
    private lateinit var browser: WebView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_law_course, container, false)

        browser = root.findViewById(R.id.web)

        if (savedInstanceState == null) {

            // Set WebView client
            browser.webChromeClient = WebChromeClient()

            browser.webViewClient = (object : WebViewClient() {

                override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                    view.loadUrl(url)
                    return true
                }
            })
            browser.loadUrl(url)
        } else
            browser.restoreState(savedInstanceState)

        return root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        browser.saveState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        browser.restoreState(savedInstanceState)
    }
}
