package com.wojcik.educourse.profile.badges.data

data class BadgeResponse (val userId : Int, val badgeId : Int, val createdAt : String, val badge : UserBadgesResponse) {
}