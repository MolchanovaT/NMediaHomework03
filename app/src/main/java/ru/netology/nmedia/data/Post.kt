package ru.netology.nmedia.data

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    var likedByMe: Boolean = false,
    var likes: Int = 0,
    var reposts: Int = 0,
    var views: Int = 0,
    var video: String? = ""
)