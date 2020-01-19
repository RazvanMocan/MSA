package com.mocan.autoreflex.ui.learning

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import java.net.URL


class DownloadFilesTask: AsyncTask<URL, Int, List<Bitmap>>() {
    override fun doInBackground(vararg url: URL?): List<Bitmap> {
        val bitmap = ArrayList<Bitmap>()
        val bmOptions = BitmapFactory.Options()
        bmOptions.inSampleSize = 1



        for (url1 in url) {
            url1!!.openStream().use { input ->
                bitmap.add(BitmapFactory.decodeStream(input, null, bmOptions)!!)
            }
        }

        return bitmap
    }

}