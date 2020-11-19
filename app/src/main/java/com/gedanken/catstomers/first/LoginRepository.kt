package com.gedanken.catstomers.first

import com.gedanken.catstomers.Repository
import com.gedanken.catstomers.first.model.remote.CredentialDTO
import com.gedanken.catstomers.first.model.remote.LogInStatus
import com.gedanken.catstomers.first.model.remote.LoginDataSource
import io.reactivex.Single

class LoginRepository (
    baseUrl: String,
    isDebugEnabled: Boolean,
    apiKey: String
) : Repository(baseUrl, isDebugEnabled, apiKey){
    private val loginDataSource: LoginDataSource = LoginDataSource(retrofit)

    inner
    class ResultLogin(val loginStatus: LogInStatus? = null, val errorMessage: String? = null) {

        fun isLogged(): Boolean {
            return loginStatus != null
        }

        fun hasError(): Boolean {
            return errorMessage != null
        }
    }

    /**
     * Login method for the user
     */
    fun postLogIn(credentialDTO: CredentialDTO): Single<ResultLogin> {
        return loginDataSource.postSignUp(credentialDTO)
            .map { loginStatus: LogInStatus -> ResultLogin(loginStatus = loginStatus) }
            .onErrorReturn { t: Throwable -> ResultLogin(errorMessage = t.message) }
    }
}