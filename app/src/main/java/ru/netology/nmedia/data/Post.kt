package ru.netology.nmedia.data

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    var likes: Int = 999,
    var reposts: Int = 26_700,
    var views: Int = 53_160_000,
    var likedByMe: Boolean = false
)