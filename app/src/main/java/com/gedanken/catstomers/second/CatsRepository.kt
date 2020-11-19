package com.gedanken.catstomers.second

import com.gedanken.catstomers.second.model.remote.CatDataSource
import com.gedanken.catstomers.Repository
import com.gedanken.catstomers.second.model.remote.BreedsResponse
import com.gedanken.catstomers.second.model.remote.BreedResponse
import io.reactivex.Single

class CatsRepository(
    baseUrl: String,
    isDebugEnabled: Boolean,
    apiKey: String
) : Repository(baseUrl, isDebugEnabled, apiKey) {

    private val catDataSource: CatDataSource = CatDataSource(retrofit)

    inner
    class Result(val breeds: List<BreedsResponse>? = null, val errorMessage: String? = null) {

        fun hasCats(): Boolean {
            return breeds != null && breeds.isNotEmpty()
        }

        fun hasError(): Boolean {
            return errorMessage != null
        }
    }

    fun getNumberOfRandomCats(page: Int, limit: Int): Single<Result> {
        return catDataSource.getPageOfCats(page, limit)
            .map { breeds: List<BreedsResponse> -> populateBreedsWithImageUrls(breeds = breeds) }
            .onErrorReturn { t: Throwable -> Result(errorMessage = t.message) }
    }
    fun getAllCatBreeds(): Single<Result> {
        return catDataSource.getAllCatBreeds()
            .map { breeds: List<BreedsResponse> -> populateBreedsWithImageUrls(breeds = breeds) }
            .onErrorReturn { t: Throwable -> Result(errorMessage = t.message) }
    }
    fun populateBreedsWithImageUrls(breeds: List<BreedsResponse>): Result {
        for (i in breeds) {
            i.breedResponses = getObservableBreedsImg(i.id).blockingGet()

        }
        return Result(breeds)
    }


    fun getObservableBreedsImg(id: String): Single<List<BreedResponse>> {
        return catDataSource.getBreedImg(id)
    }

}