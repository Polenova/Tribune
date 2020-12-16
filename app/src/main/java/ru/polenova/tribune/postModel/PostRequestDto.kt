package ru.polenova.tribune.postModel

data class PostRequestDto (

    val textPost: String? = null,
    val statusUser: StatusUser = StatusUser.NONE,
    val attachmentImage: String,
    val attachmentLink: String?

)