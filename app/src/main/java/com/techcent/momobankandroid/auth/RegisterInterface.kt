package com.techcent.momobankandroid.auth

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


interface RegisterInterface {
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
}
