package com.example.graduationdesign.model.bean.song_list_bean

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class ArtistBean(

    @SerializedName(value = "id", alternate = ["first"])
    var id: String,

    @SerializedName(value = "name", alternate = ["third"])
    var name: String
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ArtistBean> {
        override fun createFromParcel(parcel: Parcel): ArtistBean {
            return ArtistBean(parcel)
        }

        override fun newArray(size: Int): Array<ArtistBean?> {
            return arrayOfNulls(size)
        }
    }
}
