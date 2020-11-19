package com.gedanken.catstomers.second.model.remote

import com.google.gson.annotations.SerializedName

data class BreedResponse(
    // BREED
    /**
    When our catstomers enter our app, we want that they see an alphabetical ordered list of all
    the cat breeds that are available. Customers should be able to filter the breeds by country.

    Each cat breed will be shown in a "card" with a small image on the left side, and the name and
    description of the cat breed in the rest of the card.

    When a card is tapped, we will show a details page, where we will put the image, the name,
    description, country code, the temperament and Wikipedia link of the particular breed.
     */
    @SerializedName("breeds") var breeds : ArrayList<BreedsResponse>,
    @SerializedName("id") val id : String,
    @SerializedName("url") val url : String,
    @SerializedName("width") val width : Int,
    @SerializedName("height") val height : Int

) {
    override fun toString(): String {
        return "BreedResponse(breeds=$breeds, id='$id', url='$url', width=$width, height=$height)"
    }
}