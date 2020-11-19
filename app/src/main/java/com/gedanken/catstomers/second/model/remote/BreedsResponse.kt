package com.gedanken.catstomers.second.model.remote

import com.google.gson.annotations.SerializedName

data class BreedsResponse (

    @SerializedName("id") val id : String,
    @SerializedName("name") val name : String,
    @SerializedName("temperament") val temperament : String,
    @SerializedName("origin") val origin : String,
    @SerializedName("country_code") val country_code : String,
    @SerializedName("description") val description : String,
    @SerializedName("wikipedia_url") val wikipedia_url : String,
    var breedResponses: List<BreedResponse>// Although it comes only one
){
    override fun toString(): String {
        return "Breeds(id='$id', name='$name', temperament='$temperament', origin='$origin', country_code='$country_code', description='$description', json4kotlinBases=$breedResponses)"
    }
}