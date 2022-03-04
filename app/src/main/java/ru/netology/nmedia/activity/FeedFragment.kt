package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.NewPostFragment.Companion.textArg
import ru.netology.nmedia.activity.NewPostFragment.Companion.idArg
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.FragmentFeedBinding
import ru.netology.nmedia.data.Post
import ru.netology.nmedia.util.LongArg
import ru.netology.nmedia.viewmodel.PostViewModel

class FeedFragment : Fragment() {

    companion object {
        var Bundle.idArgDelete: Long? by LongArg
    }

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFeedBinding.inflate(
            inflater,
            container,
            false
        )

        val idDelete = arguments?.idArgDelete
        if (idDelete != null) viewModel.removeById(idDelete)

        val adapter = PostsAdapter(object : OnInteractionListener {

            override fun onEdit(post: Post) {
//                viewModel.edit(post)
                val text = post.content
                val id = post.id
                if (text.isNotBlank()) {
                    findNavController().navigate(
                        R.id.action_feedFragment_to_newPostFragment,
                        Bundle().apply {
                            textArg = text
                            idArg = id
                        }
                    )
                }
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

                viewModel.repostById(post.id)
            }

            override fun onView(post: Post) {
                viewModel.viewById(post.id)
            }

            override fun onPlay(post: Post) {
                val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(post.video))
                startActivity(webIntent)

            }

            override fun onContent(post: Post) {
                val id = post.id
                findNavController().navigate(
                    R.id.action_feedFragment_to_postFragment,
                    Bundle().apply {
                        idArg = id
                    }
                )
            }
        })

        binding.list.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner)
        { posts ->
            adapter.submitList(posts)
        }

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
        }

        return binding.root
    }
}