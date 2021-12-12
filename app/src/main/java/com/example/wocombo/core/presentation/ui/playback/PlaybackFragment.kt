package com.example.wocombo.core.presentation.ui.playback

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wocombo.R
import com.example.wocombo.app.MainActivity
import com.example.wocombo.common.extensions.AppBarHelper
import com.example.wocombo.common.extensions.viewInflateBinding
import com.example.wocombo.common.logs.LoggerTags
import com.example.wocombo.common.navigation.BaseNavigation
import com.example.wocombo.common.navigation.NavigationConst
import com.example.wocombo.core.presentation.model.Comment
import com.example.wocombo.core.presentation.ui.playback.adapter.CommentsAdapter
import com.example.wocombo.databinding.FragmentPlaybackBinding
import de.mateware.snacky.Snacky
import org.koin.android.ext.android.inject

class PlaybackFragment : Fragment() {

    private val binding by viewInflateBinding(FragmentPlaybackBinding::inflate)

    private val navigation: BaseNavigation by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            navigation.popBackStack()
        }
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initActionBar()
        initVideoView()
        initComments()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initComments() {
        binding.rvComments.apply {
            adapter = CommentsAdapter().apply {
                submitList(getSampleComments())
            }
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun getSampleComments(): List<Comment> {
        return listOf(
            Comment(
                avatar = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_sentiment_very_dissatisfied_24)!!,
                username = "Lilo",
                text = "Nulla at purus et eros aliquet rhoncus eu vel dolor. Mauris maximus massa dui. Nullam sed fringilla quam. Mauris dui quam, placerat nec erat et, sollicitudin tristique orci. Curabitur lacinia massa vel ligula dictum ornare ut at risus."
            ),
            Comment(
                avatar = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_sentiment_very_satisfied_24)!!,
                username = "Stich",
                text = "Nulla at purus et eros aliquet rhoncus eu vel dolor. Mauris maximus massa dui. Nullam sed fringilla quam. Mauris dui quam, placerat nec erat et, sollicitudin tristique orci. Curabitur lacinia massa vel ligula dictum ornare ut at risus."
            ),
            Comment(
                avatar =  ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_sentiment_very_dissatisfied_24)!!,
                username = "Stefan",
                text = "Nulla at purus et eros aliquet rhoncus eu vel dolor. Mauris maximus massa dui. Nullam sed fringilla quam. Mauris dui quam, placerat nec erat et, sollicitudin tristique orci. Curabitur lacinia massa vel ligula dictum ornare ut at risus."
            ),
            Comment(
                avatar =  ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_sentiment_very_satisfied_24)!!,
                username = "Federer",
                text = "Nulla at purus et eros aliquet rhoncus eu vel dolor. Mauris maximus massa dui. Nullam sed fringilla quam. Mauris dui quam, placerat nec erat et, sollicitudin tristique orci. Curabitur lacinia massa vel ligula dictum ornare ut at risus."
            ),
            Comment(
                avatar =  ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_sentiment_very_dissatisfied_24)!!,
                username = "Andrew",
                text = "Nulla at purus et eros aliquet rhoncus eu vel dolor. Mauris maximus massa dui. Nullam sed fringilla quam. Mauris dui quam, placerat nec erat et, sollicitudin tristique orci. Curabitur lacinia massa vel ligula dictum ornare ut at risus."
            ),
        )
    }

    private fun initActionBar() {
        (requireActivity() as MainActivity).supportActionBar?.run {
            AppBarHelper.setShowHideAnimationForActionBar(this)
            hide()
        }
    }

    private fun initVideoView() {
        try {
            with(binding.vvVideo) {
                val uri = Uri.parse(arguments?.getString(NavigationConst.VIDEO_URL))
                setVideoURI(uri)
                val mediaController = MediaController(requireContext()).apply {
                    setAnchorView(this@with)
                    setMediaPlayer(this@with)
                }
                setMediaController(mediaController)
                start()
            }
        } catch (e: Exception) {
            Log.e(LoggerTags.VIDEO, "Cannot play video", e)
            Snacky.builder()
                .setActivity(requireActivity())
                .setText(R.string.err_cannot_play_video)
                .setDuration(Snacky.LENGTH_SHORT)
                .info()
                .show()
        }
    }
}