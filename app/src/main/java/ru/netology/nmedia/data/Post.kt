package ru.netology.nmedia.data

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    var likes: Int = 999,
    var reposts: Int = 10,
    var views: Int = 49_999,
    var likedByMe: Boolean = false
)