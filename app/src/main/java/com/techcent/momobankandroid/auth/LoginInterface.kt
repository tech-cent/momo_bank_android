package com.techcent.momobankandroid.auth

import retrofit2.Call
import retrofit2.http.*


interface LoginInterface {
    @FormUrlEncoded
    @POST("login/")
    fun login(
        @Field("phone_number") phoneNumber: String?,
        @Field("password") password: String?
    ): Call<String?>?

    @GET("account/")
    fun getAccounts(@Header("Authorization") authHeader: String): Call<String?>?
}
