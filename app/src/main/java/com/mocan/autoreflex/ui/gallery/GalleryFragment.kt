package com.mocan.autoreflex.ui.gallery

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.provider.Browser
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.mocan.autoreflex.R
import android.webkit.WebViewClient
import android.webkit.WebChromeClient
import android.widget.Toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mocan.autoreflex.ui.main.MainMenuActivity
import java.time.Duration


class GalleryFragment : Fragment() {

    private lateinit var galleryViewModel: GalleryViewModel
    private var url:String = "https://www.drpciv.ro/dlexam"
    private lateinit var browser: WebView
//    private lateinit var fusedLocationClient: FusedLocationProviderClient


    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        galleryViewModel =
            ViewModelProviders.of(this).get(GalleryViewModel::class.java)
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.requireActivity())

        val root = inflater.inflate(R.layout.fragment_gallery, container, false)

        browser = root.findViewById(R.id.examView)

//        fusedLocationClient.lastLocation
//            .addOnSuccessListener { location : Location? ->
//                Log.e("location", location!!.longitude.toString())
//                Log.e("location", location!!.latitude.toString())
//
//                val loc = Location("")
//                loc.latitude = 45.745307
//                loc.longitude = 21.226159
//                Log.e("loc", location.distanceTo(loc).toString())
//            }

        // Load the webpage if not loaded before
        if (savedInstanceState == null) {
            // Enable javascript
            browser.settings.javaScriptEnabled = true

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