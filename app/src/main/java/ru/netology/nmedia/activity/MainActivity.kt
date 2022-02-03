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

                likesAmount.text = amountRepresentation(post.likes)
                repostsAmount.text = amountRepresentation(post.reposts)
                viewsAmount.text = amountRepresentation(post.views)
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
    }

    private fun amountRepresentation(num: Int): String {
        return when {
            num < 1_000 -> num.toString()
            num in 1_000..9999 -> "%.1f".format(num.toDouble() / 1_000) + "K"
            num in 10_000..999999 -> (num / 1_000).toString() + "K"
            num >= 1_000_000 -> "%.1f".format(num.toDouble() / 1_000_000) + "M"
            else -> num.toString()
        }
    }
}
