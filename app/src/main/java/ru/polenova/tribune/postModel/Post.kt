package ru.polenova.tribune.postModel

import android.content.SharedPreferences
import com.google.gson.Gson
import org.json.JSONArray

data class Post(
    val idUser: Long,
    val userName: String? = null,
    val dateOfCreate: String? = null,
    val statusUser: StatusUser = StatusUser.NONE,
    val linkForPost: String? = null,
    val postName: String? = null,
    val postText: String? = null,
    val idPost: Long,
    var postUpCount: Int,
    var postDownCount: Int,
    var pressedPostUp: Boolean,
    var pressedPostDown: Boolean

) {
    var upActionPerforming = false
    var downActionPerforming = false
    fun updatePost(updatedModel: Post) {
        if (idUser != updatedModel.idUser) throw IllegalAccessException("Ids are different")
        postUpCount = updatedModel.postUpCount
        pressedPostUp = updatedModel.pressedPostUp
        postDownCount = updatedModel.postDownCount
        pressedPostDown = updatedModel.pressedPostDown
    }
}

enum class StatusUser {
    PROMOTER,
    HATER,
    NONE
}

data class Users(

    val userNameReaction: String?,
    val userStatusReaction: StatusUser,
    val dateOfReaction: String?
)



