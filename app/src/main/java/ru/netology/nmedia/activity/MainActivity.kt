package ru.netology.nmedia.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()
        viewModel.data.observe(this) { post ->
            with(binding) {
                author.text = post.author
                published.text = post.published
                content.text = post.content
                likes.setImageResource(
                    if (post.likedByMe) R.drawable.ic_liked else R.drawable.ic_like
                )

                likesAmount.text = viewModel.amountRepresentation(post.likes)
                repostsAmount.text = viewModel.amountRepresentation(post.reposts)
                viewsAmount.text = viewModel.amountRepresentation(post.views)
            }

            binding.likes.setOnClickListener {
                viewModel.like()
            }

            binding.reposts.setOnClickListener {
                viewModel.repost()
            }

            binding.views.setOnClickListener {
                viewModel.view()
            }
        }

        /*val post = Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            published = "21 мая в 18:36",
            likedByMe = false
        )

        with(binding) {
            author.text = post.author
            published.text = post.published
            content.text = post.content

            if (post.likedByMe) {
                likes.setImageResource(R.drawable.ic_liked)
            }

            likesAmount.text = amountRepresentation(post.likes)

            likes.setOnClickListener {
                post.likedByMe = !post.likedByMe
                likes.setImageResource(
                    if (post.likedByMe) R.drawable.ic_liked else R.drawable.ic_like
                )
                if (post.likedByMe) post.likes++ else post.likes--
                likesAmount.text = amountRepresentation(post.likes)
            }

            repostsAmount.text = amountRepresentation(post.reposts)

            reposts.setOnClickListener {
                post.reposts++
                repostsAmount.text = amountRepresentation(post.reposts)
            }

            viewsAmount.text = amountRepresentation(post.views)

            views.setOnClickListener {
                post.views++
                viewsAmount.text = amountRepresentation(post.views)
            }
        }*/
    }
}
