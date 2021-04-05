package com.example.graduationdesign.model.bean.new_album_bean

import android.os.Parcel
import android.os.Parcelable
import com.example.graduationdesign.model.bean.song_list_bean.ArtistBean
import com.google.gson.annotations.SerializedName

data class TheNewDiscShelvesBean(
    @SerializedName("code")
    var code: Int,
    @SerializedName("monthData")
    var monthData: ArrayList<SingleMonthData>,
    @SerializedName("hasMore")
    var hasMore: Boolean
)

data class SingleMonthData(
    @SerializedName("id")
    var id: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("picUrl")
    var imageUrl: String,
    @SerializedName("artists")
    var artists: ArrayList<ArtistBean>,
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.createTypedArrayList(ArtistBean.CREATOR)!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(imageUrl)
        parcel.writeTypedList(artists)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SingleMonthData> {
        override fun createFromParcel(parcel: Parcel): SingleMonthData {
            return SingleMonthData(parcel)
        }

        override fun newArray(size: Int): Array<SingleMonthData?> {
            return arrayOfNulls(size)
        }
    }
}
