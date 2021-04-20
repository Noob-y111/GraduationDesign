package com.example.graduationdesign.model.bean.user_info

data class UserDetailResponse(
    var code: Int,
    var level: Int,
    var createDays: Int,
    var createTime: Long,
    var profile: Profile
)

data class Profile(
    var nickname: String,
    var backgroundUrl: String,
    var avatarUrl: String,
    var province: String,
    var city: String
)
