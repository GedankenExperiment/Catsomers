package com.gedanken.catstomers.second.model.remote


import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

class CatDataSource (retrofit: Retrofit){

    private val api: CatsApi = retrofit.create(CatsApi::class.java)

    fun getAllCatBreeds() =
        api.getAllCatBreeds()
    
    fun getPageOfCats(page: Int, limit: Int) =
        api.getNumberOfRandomCats(page, limit)

    fun getBreedImg(id: String) =
        api.getBreedImg(id)

    interface CatsApi {

        @GET("breeds")
        fun getAllCatBreeds(): Single<List<BreedsResponse>>
        
        @GET("breeds")
        fun getNumberOfRandomCats(@Query("page") page: Int, @Query("limit") limit: Int): Single<List<BreedsResponse>>
        
        @GET("images/search/")
        fun getBreedImg(@Query("breeds_ids") id: String): Single<List<BreedResponse>>

    }
}