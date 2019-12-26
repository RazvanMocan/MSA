package com.mocan.autoreflex.ui.gallery

import android.os.Bundle
import android.provider.Browser
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.mocan.autoreflex.R
import android.webkit.WebViewClient
import android.webkit.WebChromeClient
import com.google.android.material.floatingactionbutton.FloatingActionButton


class GalleryFragment : Fragment() {

    private lateinit var galleryViewModel: GalleryViewModel
    private var url:String = "https://www.drpciv.ro/dlexam"
    private lateinit var browser: WebView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        galleryViewModel =
            ViewModelProviders.of(this).get(GalleryViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_gallery, container, false)

        browser = root.findViewById(R.id.examView)


        // Load the webpage if not loaded before
        if (savedInstanceState == null) {
            // Enable javascript
            browser.settings.setJavaScriptEnabled(true)

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