package com.techcent.momobankandroid.api

import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    /**
     * @POST("signup/") declares an HTTP POST request to sign up a user
     * @Field("first_name") annotation on the firstName field from the form body
     * @Field("last_name") annotation on the lastName field from the form body
     * @Field("phone_number") annotation on the phoneNumber field from the form body
     * @Field("password") annotation on the password field from the form body
     * @Field("nin") annotation on the National ID Number field from the form body
     * @Field("dob") annotation on the dateOfBirth field from the form body
     */
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


    /**
     * @POST("login/") declares an HTTP POST request to sign in a user
     * @Field("phone_number") annotation on the phoneNumber field from the form body
     * @Field("password") annotation on the password field from the form body
     */
    @FormUrlEncoded
    @POST("login/")
    fun login(
        @Field("phone_number") phoneNumber: String?,
        @Field("password") password: String?
    ): Call<String?>?


    /**
     * @GET("account/") declares an HTTP GET request to retrieve a user's accounts
     * @Header("Authorization") annotation on the header provides the `Bearer` token
     */
    @GET("account/")
    fun getAccounts(@Header("Authorization") authHeader: String): Call<String?>?


    /**
     * @GET("account/{account_id}/transactions") declares an HTTP GET request to retrieve an account's transactions
     * @Header("Authorization") annotation on the header provides the `Bearer` token
     * @Path("account_id") annotation on the accountId parameter marks it as a
     * replacement for the {accountId} placeholder in the @GET path
     */
    @GET("account/{account_id}/transactions")
    fun getUserAccountTransactions(
        @Header("Authorization") authHeader: String,
        @Path("account_id") id: Int
    ): Call<String?>?


    /**
     * @POST("transactions/") declares an HTTP POST request to create a transaction
     * @Header("Authorization") annotation on the header provides the `Bearer` token
     * @Field("account") annotation on the account field from the form body
     * @Field("type") annotation on the account type field from the form body
     * @Field("amount") annotation on the amount to be transacted from the form body
     */
    @FormUrlEncoded
    @POST("transactions/")
    fun transact(
        @Header("Authorization") authHeader: String,
        @Field("account") account: Int,
        @Field("type") type: String,
        @Field("amount") amount: Int
    ): Call<String?>?


    /**
     * @GET("profile/") declares an HTTP GET request to retrieve a user's profile
     * @Header("Authorization") annotation on the header provides the `Bearer` token
     */
    @GET("profile/")
    fun getProfile(@Header("Authorization") authHeader: String): Call<String?>?
}