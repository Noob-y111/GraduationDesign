package com.example.graduationdesign.model.bean.playlist_bean

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class ListSpecies(
    @SerializedName("code")
    var code: Int,
    @SerializedName("total")
    var total: Int,
    @SerializedName("more")
    var more: Boolean,
    @SerializedName("cat")
    var category: String,
    @SerializedName("playlists")
    var playlists: ArrayList<Playlist>
)

data class Playlist(
    @SerializedName("id")
    var id: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("userId")
    var userId: String?,
    @SerializedName("coverImgUrl")
    var imageUrl: String,
    @SerializedName("playCount")
    var playCount: String
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(userId)
        parcel.writeString(imageUrl)
        parcel.writeString(playCount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Playlist> {
        override fun createFromParcel(parcel: Parcel): Playlist {
            return Playlist(parcel)
        }

        override fun newArray(size: Int): Array<Playlist?> {
            return arrayOfNulls(size)
        }
    }
}
