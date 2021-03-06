package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.NewPostFragment.Companion.textArg
import ru.netology.nmedia.databinding.FragmentPostBinding
import ru.netology.nmedia.util.LongArg
import ru.netology.nmedia.viewmodel.PostViewModel

class PostFragment : Fragment() {

    companion object {
        var Bundle.idArg: Long by LongArg
    }

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentPostBinding.inflate(
            inflater,
            container,
            false
        )

        val id = arguments?.idArg ?: -1

        viewModel.data.observe(viewLifecycleOwner)
        { posts ->
            val post = posts.singleOrNull { it.id == id } ?: return@observe

            binding.postContent.apply {
                author.text = post.author
                published.text = post.published
                content.text = post.content

                like.isChecked = post.likedByMe

                like.text = amountRepresentation(post.likes)

                repost.text = amountRepresentation(post.reposts)

                view.text = amountRepresentation(post.views)

                like.setOnClickListener { viewModel.likeById(post.id) }

                repost.setOnClickListener {
                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, post.content)
                        type = "text/plain"
                    }

                    val shareIntent =
                        Intent.createChooser(intent, getString(R.string.chooser_share_post))
                    startActivity(shareIntent)
                    viewModel.repostById(post.id) }

                view.setOnClickListener { viewModel.viewById(post.id) }

                menu.setOnClickListener()
                {
                    PopupMenu(it.context, it).apply {
                        inflate(R.menu.options_post)
                        setOnMenuItemClickListener { item ->
                            when (item.itemId) {
                                R.id.remove -> {
                                    findNavController().navigateUp()
                                    viewModel.removeById(post.id)
                                    true
                                }
                                R.id.edit -> {
                                    viewModel.edit(post)

                                    val text = post.content
                                    if (text.isNotBlank()) {
                                        findNavController().navigate(
                                            R.id.action_postFragment_to_newPostFragment,
                                            Bundle().apply {
                                                textArg = text
                                            }
                                        )
                                    }
                                    true
                                }
                                else -> false
                            }
                        }
                    }.show()
                }

                if (post.video.isNullOrBlank()) {
                    groupVideo.visibility = View.GONE
                } else groupVideo.visibility = View.VISIBLE

                video.setOnClickListener()
                {
                    val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(post.video))
                    startActivity(webIntent)
                }

                play.setOnClickListener()
                {
                    val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(post.video))
                    startActivity(webIntent)
                }
            }
        }

        return binding.root
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

