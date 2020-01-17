package com.techcent.momobankandroid.models

import android.os.Parcel
import android.os.Parcelable

class Bank() : Parcelable {
    var id: Int? = null
    var name: String? = null
    var tin: String? = null
    var district: String? = null
    var country: String? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readValue(Int::class.java.classLoader) as? Int
        name = parcel.readString()
        tin = parcel.readString()
        district = parcel.readString()
        country = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(name)
        parcel.writeString(tin)
        parcel.writeString(district)
        parcel.writeString(country)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Bank> {
        override fun createFromParcel(parcel: Parcel): Bank {
            return Bank(parcel)
        }

        override fun newArray(size: Int): Array<Bank?> {
            return arrayOfNulls(size)
        }
    }
}
