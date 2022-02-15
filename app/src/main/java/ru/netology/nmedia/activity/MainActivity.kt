package ru.netology.nmedia.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import ru.netology.nmedia.adapter.*
import ru.netology.nmedia.data.Post
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.viewmodel.PostViewModel
import androidx.activity.result.launch
import ru.netology.nmedia.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()

        val adapter = PostsAdapter(object : OnInteractionListener {
            override fun onEdit(post: Post) {
                viewModel.edit(post)
            }

            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun onRepost(post: Post) {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, post.content)
                    type = "text/plain"
                }

                val shareIntent =
                    Intent.createChooser(intent, getString(R.string.chooser_share_post))
                startActivity(shareIntent)
            }

            override fun onView(post: Post) {
                viewModel.viewById(post.id)
            }
        })
        binding.list.adapter = adapter
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }

        val newPostLauncher = registerForActivityResult(NewPostResultContract()) { result ->
            result ?: return@registerForActivityResult
            viewModel.changeContent(result)
            viewModel.save()
        }

        binding.fab.setOnClickListener {
            newPostLauncher.launch()
        }
    }

}
// super.onCreate(savedInstanceState)
// val binding = ActivityMainBinding.inflate(layoutInflater)
// setContentView(binding.root)
//
// binding.group.visibility = View.GONE
//
// val viewModel: PostViewModel by viewModels()
// val adapter = PostsAdapter(object : OnInteractionListener {
//
// override fun onLike(post: Post) {
// viewModel.likeById(post.id)
// }
//
// override fun onRepost(post: Post) {
// viewModel.repostById(post.id)
// }
//
// override fun onView(post: Post) {
// viewModel.viewById(post.id)
// }
//
// override fun onEdit(post: Post) {
// viewModel.edit(post)
// binding.group.visibility = View.VISIBLE
// }
//
// override fun onRemove(post: Post) {
// viewModel.removeById(post.id)
// }
// })
// binding.list.adapter = adapter
// viewModel.data.observe(this, adapter::submitList)
//
// viewModel.edited.observe(this) { post ->
// if (post.id == 0L) {
// return@observe
// }
// with(binding.content) {
// requestFocus()
// setText(post.content)
// }
// }
//
// binding.save.setOnClickListener {
// with(binding.content) {
// if (text.isNullOrBlank()) {
// Toast.makeText(
// this@MainActivity,
// context.getString(R.string.error_empty_content),
// Toast.LENGTH_SHORT
// ).show()
// return@setOnClickListener
// }
//
// viewModel.changeContent(text.toString())
// viewModel.save()
//
// setText("")
// clearFocus()
// AndroidUtils.hideKeyboard(this)
//
// binding.group.visibility = View.GONE
// }
// }
//
// binding.cancelEdit.setOnClickListener {
// with(binding.content) {
// setText("")
// clearFocus()
// AndroidUtils.hideKeyboard(this)
// }
// binding.group.visibility = View.GONE
// }
// }