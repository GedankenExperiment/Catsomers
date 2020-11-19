package com.gedanken.catstomers

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.gedanken.catstomers.second.viewmodel.BreedsViewModel


class MainActivity : AppCompatActivity(), ViewModelStoreOwner {

    // public static fields in a companion object because im a horrible person
    companion object {
        // the server url endpoint
        const val serverUrl = "https://api.thecatapi.com/v1/"

        // TODO save it on shared preferences after getting it from a Sign Up request
        const val apiKey = "e02b9523-a545-470e-aa3e-361372d52a07"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        val breedsViewModel = ViewModelProvider(this).get(BreedsViewModel::class.java)
        breedsViewModel.getAllCatBreeds()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}