package com.techcent.momobankandroid.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class User() : Parcelable {
    var id: Int? = null
    var email: String? = null

    @SerializedName("phone_number")
    var phoneNumber: String? = null

    @SerializedName("first_name")
    var firstName: String? = null

    @SerializedName("last_name")
    var lastName: String? = null

    var nin: String? = null
    var dob: String? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readValue(Int::class.java.classLoader) as? Int
        email = parcel.readString()
        phoneNumber = parcel.readString()
        firstName = parcel.readString()
        lastName = parcel.readString()
        nin = parcel.readString()
        dob = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(email)
        parcel.writeString(phoneNumber)
        parcel.writeString(firstName)
        parcel.writeString(lastName)
        parcel.writeString(nin)
        parcel.writeString(dob)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }

}