package com.techcent.momobankandroid.models

import android.os.Parcel
import android.os.Parcelable

class Account() : Parcelable {
    var id: Int? = null
    var type: String? = null
    var balance: Double = 0.00
    var bank: Int = 1

    constructor(parcel: Parcel) : this() {
        id = parcel.readValue(Int::class.java.classLoader) as? Int
        type = parcel.readString()
        balance = parcel.readDouble()
        bank = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(type)
        parcel.writeDouble(balance)
        parcel.writeInt(bank)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Account> {
        override fun createFromParcel(parcel: Parcel): Account {
            return Account(parcel)
        }

        override fun newArray(size: Int): Array<Account?> {
            return arrayOfNulls(size)
        }
    }
}