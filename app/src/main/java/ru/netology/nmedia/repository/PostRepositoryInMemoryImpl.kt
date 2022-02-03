package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.data.Post

class PostRepositoryInMemoryImpl: PostRepository {
    private var post = Post(
        id = 1,
        author = "Нетология. Университет интернет-профессий будущего",
        content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
        published = "21 мая в 18:36",
        likedByMe = false
    )
    private val data = MutableLiveData(post)

    override fun get(): LiveData<Post> = data

    override fun like() {
        post = post.copy(likedByMe = !post.likedByMe)
        if (post.likedByMe) post.likes++ else post.likes--
        data.value = post
    }

    override fun repost() {
        post = post.copy()
        post.reposts++
        data.value = post
    }

    override fun view() {
        post = post.copy()
        post.views++
        data.value = post
    }

    override fun amountRepresentation(num: Int): String {
        return when {
            num < 1_000 -> num.toString()
            num in 1_000..9999 -> "%.1f".format(num.toDouble() / 1_000) + "K"
            num in 10_000..999999 -> (num / 1_000).toString() + "K"
            num >= 1_000_000 -> "%.1f".format(num.toDouble() / 1_000_000) + "M"
            else -> num.toString()
        }
    }
}