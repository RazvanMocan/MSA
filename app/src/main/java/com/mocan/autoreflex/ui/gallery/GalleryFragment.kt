package com.mocan.autoreflex.ui.gallery

import android.os.Bundle
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        galleryViewModel =
            ViewModelProviders.of(this).get(GalleryViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_gallery, container, false)

        val browser:WebView = root.findViewById(R.id.examView)

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
        // Load the webpage
        browser.loadUrl("https://www.drpciv.ro/dlexam")

        return root
    }
}