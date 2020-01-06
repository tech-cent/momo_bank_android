package com.techcent.momobankandroid.auth

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


interface LoginInterface {
    @FormUrlEncoded
    @POST("login/")
    fun login(
        @Field("phone_number") phoneNumber: String?,
        @Field("password") password: String?
    ): Call<String?>?
}
