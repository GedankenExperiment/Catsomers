package com.gedanken.catstomers.third.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.gedanken.catstomers.GlideApp
import com.gedanken.catstomers.R
import com.gedanken.catstomers.Utils
import com.gedanken.catstomers.second.model.remote.BreedsResponse
import com.gedanken.catstomers.second.viewmodel.BreedsViewModel
import kotlinx.android.synthetic.main.fragment_third.*

/**
 * A simple [Fragment] subclass as the third destination in the navigation.
 */
class BreedDetailsFragment : Fragment() {


    private lateinit var breedsViewModel: BreedsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //When a card is tapped, we will show a details page, where we will put the image, the name,
        //description, country code, the temperament and Wikipedia link of the particular breed.
        return inflater.inflate(R.layout.fragment_third, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            breedsViewModel = ViewModelProvider(it).get(BreedsViewModel::class.java)
        }

        breedsViewModel.getSelectedBreed().observe(viewLifecycleOwner, Observer { onResult(it) })
        breedsViewModel.errorMessage.observe(viewLifecycleOwner, Observer { onError(it) })

        view.findViewById<Button>(R.id.detail_button_third).setOnClickListener {
            findNavController().navigate(R.id.action_ThirdFragment_to_SecondFragment)
        }
    }

    private fun onResult(breed: BreedsResponse) {

        // Not doing anything yet with this list except a toast
        //Toast.makeText(activity, "Got ${breed} breeds", Toast.LENGTH_SHORT).show()

        val circularProgressDrawable = Utils.createSpinner(detail_image.context)
        GlideApp.with(detail_image.context)
            .load(breed.breedResponses.first().url)
            .placeholder(circularProgressDrawable)
            .error(R.drawable.ic_launcher_foreground)
            .into(detail_image)

        detail_name.text = breed.name
        detail_description.text = breed.description
        detail_country_code.text = breed.country_code
        detail_temperament.text = breed.temperament
        detail_wikipedia_link.text = breed.wikipedia_url
    }
    private fun onError(error: String) {
        error.let {
            if (!it.isBlank()) {
                Toast.makeText(activity, error, Toast.LENGTH_SHORT).show()
            }
        }
    }
}