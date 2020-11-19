package com.gedanken.catstomers.first.model.remote

import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.http.*

class LoginDataSource (retrofit: Retrofit) {
    private val api: LoginApi = retrofit.create(LoginApi::class.java)

    fun postSignUp(credentialDTO: CredentialDTO) =
        api.postSignUp(credentialDTO)

    interface LoginApi {
        @POST("login")
        fun postSignUp(@Body credentialDTO: CredentialDTO): Single<LogInStatus>
    }
}