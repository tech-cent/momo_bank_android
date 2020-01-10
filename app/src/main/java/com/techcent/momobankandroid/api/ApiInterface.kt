package com.techcent.momobankandroid.api

import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {
    @FormUrlEncoded
    @POST("signup/")
    fun signUp(
        @Field("first_name") firstName: String?,
        @Field("last_name") lastName: String?,
        @Field("phone_number") phoneNumber: String?,
        @Field("password") password: String?,
        @Field("nin") NIN: String?,
        @Field("dob") dateOfBirth: String?
    ): Call<String?>?

    @FormUrlEncoded
    @POST("login/")
    fun login(
        @Field("phone_number") phoneNumber: String?,
        @Field("password") password: String?
    ): Call<String?>?

    @GET("account/")
    fun getAccounts(@Header("Authorization") authHeader: String): Call<String?>?

    @GET("account/{account_id}/transactions")
    fun getUserAccountTransactions(
        @Header("Authorization") authHeader: String,
        @Path("account_id") id: Int
    ): Call<String?>?
}