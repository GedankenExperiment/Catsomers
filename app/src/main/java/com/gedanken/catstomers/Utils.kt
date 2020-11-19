package com.gedanken.catstomers

import android.content.Context
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import io.reactivex.Observable

object Utils {
    /**
     *  a basic spinner created programmatically
     *  this is something that you typically stumble upon on Stack Overflow
     */
    fun createSpinner(context: Context): CircularProgressDrawable {
        val circularProgressDrawable = CircularProgressDrawable(context)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()
        return circularProgressDrawable
    }
}