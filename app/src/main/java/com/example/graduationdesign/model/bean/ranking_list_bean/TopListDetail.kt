package com.example.graduationdesign.model.bean.ranking_list_bean

import android.os.Parcel
import android.os.Parcelable
import com.example.graduationdesign.model.bean.song_list_bean.ArtistBean
import com.example.graduationdesign.model.bean.song_list_bean.SongBean
import com.google.gson.annotations.SerializedName

data class TopListDetail(
    @SerializedName("code")
    var code: Int,
    @SerializedName("list")
    var listDetail: ArrayList<ListDetail>,
//    @SerializedName("artistToplist")
//    var artistTopList: ArrayList<ArtistTopList>,
//    @SerializedName("rewardToplist")
//    var rewardTopList: ArrayList<RewardTopList>
)

data class ListDetail(
    @SerializedName("id")
    var id: String?,
    @SerializedName("name")
    var name: String?,
    @SerializedName("coverImgUrl")
    var coverImgUrl: String?,
    @SerializedName("updateFrequency")
    var updateFrequency: String?,
    @SerializedName("description")
    var description: String?,
    @SerializedName("updateTime")
    var updateTime: String?,
    @SerializedName("tracks")
    var tracks: ArrayList<Track>,
    @SerializedName("trackCount")
    var trackCount: Int

): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.createTypedArrayList(Track.CREATOR) as ArrayList<Track>,
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(coverImgUrl)
        parcel.writeString(updateFrequency)
        parcel.writeString(description)
        parcel.writeString(updateTime)
        parcel.writeTypedList(tracks)
        parcel.writeInt(trackCount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ListDetail> {
        override fun createFromParcel(parcel: Parcel): ListDetail {
            return ListDetail(parcel)
        }

        override fun newArray(size: Int): Array<ListDetail?> {
            return arrayOfNulls(size)
        }
    }
}

data class ArtistTopList(
    @SerializedName("name")
    var name: String,
    @SerializedName("updateFrequency")
    var updateFrequency: String,
    @SerializedName("coverUrl")
    var coverImgUrl: String,
    @SerializedName("artists")
    var artists: ArrayList<ArtistBean>
)

data class RewardTopList(
    @SerializedName("name")
    var name: String,
    @SerializedName("coverUrl")
    var coverImgUrl: String,
    @SerializedName("songs")
    var songs: ArrayList<SongBean>
)

data class Track(
    @SerializedName("first")
    var name: String?,
    @SerializedName("second")
    var artist: String?
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(artist)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Track> {
        override fun createFromParcel(parcel: Parcel): Track {
            return Track(parcel)
        }

        override fun newArray(size: Int): Array<Track?> {
            return arrayOfNulls(size)
        }
    }
}
