package ru.polenova.tribune.postModel

data class Post(
    val userName: String? = null,
    val dateOfCreate: String? = null,
    val statusUser: StatusUser = StatusUser.NONE,
    val link: String? = null,
    val postName: String? = null,
    val postText: String? = null,
    val idPost: Long,
    val user: User,
    var postUpCount: Int,
    var postDownCount: Int,
    var pressedPostUp: Boolean,
    var pressedPostDown: Boolean

) {
    var upActionPerforming = false
    var downActionPerforming = false
    fun updatePost(updatedModel: Post) {
        if (idPost != updatedModel.idPost) throw IllegalAccessException("Ids are different")
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

data class User(
    val idUser: Long,
    val userName: String? = null,
    val attachmentImage: String?,
    val status: StatusUser,
    val userNameReaction: String?,
    val userStatusReaction: StatusUser,
    val dateOfReaction: String?,
    val token: String?,
    val readOnly: Boolean
)

data class UsersReactionModel(
    val idUser: Long,
    val userName: String,
    val dateOfReaction: String?,
    val status: StatusUser,
    val reaction: Reaction
)

enum class Reaction {
    UP,
    DOWN
}

