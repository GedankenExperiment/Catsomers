package com.gedanken.catstomers.first.viewmodel

import android.util.Log
import androidx.databinding.library.BuildConfig
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gedanken.catstomers.MainActivity
import com.gedanken.catstomers.first.LoginRepository
import com.gedanken.catstomers.first.model.remote.CredentialDTO
import com.gedanken.catstomers.first.model.remote.LogInStatus
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class LoginViewModel : ViewModel() {
    val progressVisivility = MutableLiveData<Boolean>()

    private val compositeDisposableOnDestroy = CompositeDisposable()
    private var latestCatCall: Disposable? = null

    // the list that will be observed by the activity


    val loggedIn = MutableLiveData<LogInStatus>()

    // the error message observed
    val errorMessage = MutableLiveData<String>()
    val loginRepository = LoginRepository(
        MainActivity.serverUrl, BuildConfig.DEBUG,
        MainActivity.apiKey
    )

    fun logIn(email: String, password: String) {
        progressVisivility.value = true
        // initialising the repository class with the necessary information


        // stopping the last call if it's already running (optional)
        latestCatCall?.dispose()

        val credential = CredentialDTO(email, password)
        latestCatCall =
            loginRepository.postLogIn(credential).subscribeOn(Schedulers.io())
                .doOnSubscribe {
                    compositeDisposableOnDestroy.add(it)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { result ->
                    when {
                        result.hasError() -> result.errorMessage?.let {
                            // anyone who observes this will be notified of the change automatically
                            errorMessage.postValue("Error login in $it")
                        }
                            ?: run {
                                // anyone who observes this will be notified of the change automatically
                                errorMessage.postValue("Null error")
                            }
                        result.isLogged() -> result.let {
                            // anyone who observes this will be notified of the change automatically
                            loggedIn.postValue(it.loginStatus)

                            // clearing the error if it existed (hacky and optional)
                            errorMessage.postValue("")
                        }

                        else -> {
                            // anyone who observes this will be notified of the change automatically
                            errorMessage.postValue("Empty login")
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