package ru.zabster.safepassapp.screens.fragments.data_list.data

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator

/**
 * UI data for pass
 *
 * @param id id from db
 * @param name name for pass
 * @param description info for pass
 * @param categoryId category id from db
 */
data class CredentialData(
    val id: Long,
    val name: String,
    val description: String,
    val hashPass: String,
    val categoryId: Long,
    var pass: String = ""
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readLong(),
        parcel.readString().toString(),
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(hashPass)
        parcel.writeLong(categoryId)
        parcel.writeString(pass)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Creator<CredentialData> {

        override fun createFromParcel(parcel: Parcel): CredentialData {
            return CredentialData(parcel)
        }

        override fun newArray(size: Int): Array<CredentialData?> {
            return arrayOfNulls(size)
        }
    }
}
