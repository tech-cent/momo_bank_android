package com.techcent.momobankandroid.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class Transaction() : Parcelable {
    var id: Int? = null
    var type: String? = null
    var amount: Double = 0.00

    @SerializedName("prev_balance")
    var prevBalance: Double = 0.00

    @SerializedName("new_balance")
    var newBalance: Double = 0.00

    var status: String? = null

    @SerializedName("date_created")
    var dateCreated: String? = null

    var account: Int? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readValue(Int::class.java.classLoader) as? Int
        type = parcel.readString()
        amount = parcel.readDouble()
        prevBalance = parcel.readDouble()
        newBalance = parcel.readDouble()
        status = parcel.readString()
        dateCreated = parcel.readString()
        account = parcel.readValue(Int::class.java.classLoader) as? Int
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(type)
        parcel.writeDouble(amount)
        parcel.writeDouble(prevBalance)
        parcel.writeDouble(newBalance)
        parcel.writeString(status)
        parcel.writeString(dateCreated)
        parcel.writeValue(account)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Transaction> {
        override fun createFromParcel(parcel: Parcel): Transaction {
            return Transaction(parcel)
        }

        override fun newArray(size: Int): Array<Transaction?> {
            return arrayOfNulls(size)
        }
    }

}
