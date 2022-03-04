package ru.netology.nmedia.viewmodel

import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.data.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryFileImpl
import android.app.Application
import androidx.lifecycle.AndroidViewModel

private val empty = Post(
    id = 0,
    content = "",
    author = "",
    likedByMe = false,
    published = ""
)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository = PostRepositoryFileImpl(application)
    val data = repository.getAll()
    private val edited by lazy { MutableLiveData(empty) }

    fun likeById(id: Long) = repository.likeById(id)

    fun repostById(id: Long) = repository.repostById(id)
    fun viewById(id: Long) = repository.viewById(id)

    fun removeById(id: Long) = repository.removeById(id)

    fun save() {
        edited.value?.let {
            repository.save(it)
        }
        edited.value = empty
    }

//    fun edit(post: Post) {
//        edited.value = post
//    }

    fun edit(id: Long) {
        edited.value = repository.editById(id)
    }

    fun changeContent(content: String) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value = edited.value?.copy(content = text)
    }
}