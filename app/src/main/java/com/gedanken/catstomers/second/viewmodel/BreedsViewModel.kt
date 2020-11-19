package com.gedanken.catstomers.second.viewmodel

import android.util.Log
import androidx.databinding.library.BuildConfig
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gedanken.catstomers.MainActivity.Companion.apiKey
import com.gedanken.catstomers.MainActivity.Companion.serverUrl
import com.gedanken.catstomers.second.CatsRepository
import com.gedanken.catstomers.second.model.remote.BreedsResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class BreedsViewModel : ViewModel() {

    private val noOfItemsPerPage: Int = 6
    val progressVisivility = MutableLiveData<Boolean>()
    private val compositeDisposableOnDestroy = CompositeDisposable()
    private var latestCatCall: Disposable? = null
    // the error message observed
    val errorMessage = MutableLiveData<String>()
    val catsRepository = CatsRepository(serverUrl, BuildConfig.DEBUG, apiKey)

    // the list that will be observed by the activity
    val breedList = MutableLiveData<List<BreedsResponse>>()
    // the selected cat to show its details
    val breedSelected = MutableLiveData<BreedsResponse>()


    /**
     * select a breed for details
     */
    fun selectBreed(breedsResponse: BreedsResponse) {
        breedSelected.setValue(breedsResponse)
    }

    /**
     * gets the selected breed for details
     */
    fun getSelectedBreed(): MutableLiveData<BreedsResponse> {
        return breedSelected
    }

    fun getCatBreedsPageNo(page: Int) {
        progressVisivility.value = true
        // initialising the repository class with the necessary information
        // stopping the last call if it's already running (optional)
        latestCatCall?.dispose()
        // perform the API call
        latestCatCall =
            catsRepository.getNumberOfRandomCats(page, noOfItemsPerPage)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe {
                    compositeDisposableOnDestroy.add(it)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { result ->
                    when {
                        result.hasError() -> result.errorMessage?.let {
                            // anyone who observes this will be notified of the change automatically
                            errorMessage.postValue("Error getting cats $it")
                        }
                            ?: run {
                                // anyone who observes this will be notified of the change automatically
                                errorMessage.postValue("Null error")
                            }
                        result.hasCats() -> result.breeds?.let {
                            // anyone who observes this will be notified of the change automatically
                            breedList.postValue(it)

                            // clearing the error if it existed (hacky and optional)
                            errorMessage.postValue("")
                        }
                            ?: run {
                                // anyone who observes this will be notified of the change automatically
                                errorMessage.postValue("Null list of cats")
                            }
                        else -> {
                            // anyone who observes this will be notified of the change automatically
                            errorMessage.postValue("No cats available")
                        }
                    }
                }
    }
    fun getAllCatBreeds() {
        progressVisivility.value = true
        // initialising the repository class with the necessary information
        // stopping the last call if it's already running (optional)
        latestCatCall?.dispose()
        // perform the API call
        latestCatCall =
            catsRepository.getAllCatBreeds()
                .subscribeOn(Schedulers.io())
                .doOnSubscribe {
                    compositeDisposableOnDestroy.add(it)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { result ->
                    when {
                        result.hasError() -> result.errorMessage?.let {
                            // anyone who observes this will be notified of the change automatically
                            errorMessage.postValue("Error getting cats $it")
                        }
                            ?: run {
                                // anyone who observes this will be notified of the change automatically
                                errorMessage.postValue("Null error ")
                            }
                        result.hasCats() -> result.breeds?.let {
                            // anyone who observes this will be notified of the change automatically
                            breedList.postValue(it)

                            // clearing the error if it existed (hacky and optional)
                            errorMessage.postValue("")
                        }
                            ?: run {
                                // anyone who observes this will be notified of the change automatically
                                errorMessage.postValue("Null list of cats")
                            }
                        else -> {
                            // anyone who observes this will be notified of the change automatically
                            errorMessage.postValue("No cats available")
                        }
                    }
                }
    }
    // clearing the collection of disposables = no memory leaks no matter what
    override fun onCleared() {
        compositeDisposableOnDestroy.clear()
        Log.d("TAG", "Clearing ViewModel")
        super.onCleared()
    }
}