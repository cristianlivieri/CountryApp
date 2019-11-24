package it.prima.countries.model

import android.os.Parcel
import android.os.Parcelable

class Country(
    val name: String,
    val region: String,
    val alpha2Code: String,
    val languages: ArrayList<Languages>
) :
    Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        arrayListOf<Languages>().apply {
            parcel.readArrayList(Languages::class.java.classLoader)
        }
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(region)
        parcel.writeString(alpha2Code)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Country> {
        override fun createFromParcel(parcel: Parcel): Country {
            return Country(parcel)
        }

        override fun newArray(size: Int): Array<Country?> {
            return arrayOfNulls(size)
        }
    }

}