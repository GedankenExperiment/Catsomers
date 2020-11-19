package com.gedanken.catstomers.second.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.gedanken.catstomers.R
import com.gedanken.catstomers.Utils
import com.gedanken.catstomers.GlideApp
import com.gedanken.catstomers.second.model.remote.BreedsResponse
import com.google.android.material.textview.MaterialTextView
import java.util.*
import kotlin.collections.ArrayList

class BreedRecyclerViewAdapter(private var countryList: ArrayList<BreedsResponse>, private val breedsFragment: BreedsFragment) :
    RecyclerView.Adapter<BreedRecyclerViewAdapter.BreedViewHolder>(), Filterable {

    private var countryFilterList : MutableList<BreedsResponse> = countryList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BreedViewHolder {
        return BreedViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.breed_simple_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = countryFilterList .size

    override fun onBindViewHolder(holder: BreedViewHolder, position: Int) {
        holder.bind(countryFilterList [position])
    }

    fun setNetCats(data: List<BreedsResponse>) {
        countryFilterList .addAll(data)
        notifyDataSetChanged()
    }
    inner class BreedViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(breedResponse: BreedsResponse) {
            val photoView: ImageView =
                itemView.findViewById<ImageView>(R.id.breed_image)
            val circularProgressDrawable = Utils.createSpinner(itemView.context)
            GlideApp.with(itemView.context)
                .load(breedResponse.breedResponses.first().url)
                .placeholder(circularProgressDrawable)
                .error(R.drawable.ic_launcher_foreground)
                .into(photoView)

            val nameView: MaterialTextView =
                itemView.findViewById<MaterialTextView>(R.id.name)
            nameView.text = breedResponse.name

            val originView: MaterialTextView =
                itemView.findViewById<MaterialTextView>(R.id.origin)
            originView.text =  breedResponse.origin


            val descriptionView: MaterialTextView =
                itemView.findViewById<MaterialTextView>(R.id.description)
                descriptionView.text = breedResponse.description.take(110).plus("...")

            val constraintLayout: ConstraintLayout =
                itemView.findViewById(R.id.breed_row)
            constraintLayout.setOnClickListener {
                breedsFragment.selectBreed(breedResponse)
            }
        }
    }
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    countryFilterList = countryList
                } else {
                    val resultList = ArrayList<BreedsResponse>()
                    for (row in countryList) {
                        if (row.origin.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))) {
                            resultList.add(row)
                        }
                    }
                    countryFilterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = countryFilterList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                countryFilterList = results?.values as ArrayList<BreedsResponse>
                notifyDataSetChanged()
            }

        }
    }
}