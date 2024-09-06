package com.example.quote.ui.fragment

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.quote.R
import com.example.quote.data.entity.QuotesRespondItem
import com.example.quote.databinding.FragmentHomeBinding
import com.example.quote.ui.viewmodel.HomeViewModel
import com.example.quote.util.Resource
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        setNavigation()
        viewModel.getQuotes()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.quotes.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    initialAnimation()
                    adaptLayout(it.data)
                    viewModel.checkRoom(it.data.quote)
                }

                is Resource.Error -> {
                    Snackbar.make(requireView(), it.exception.message!!, Snackbar.LENGTH_SHORT)
                        .setAction("Retry") {
                            viewModel.getQuotes()
                        }.show()
                }

                else -> {

                }
            }
        }

        viewModel.checkDb.observe(viewLifecycleOwner) {
            if (it) {
                binding.btnSave.setImageResource(R.drawable.baseline_cloud_done_24)
                binding.txtAction.text = "Remove"
            } else {
                binding.btnSave.setImageResource(R.drawable.baseline_cloud_upload_24)
                binding.txtAction.text = "Save"
            }
        }

    }

    private fun adaptLayout(quote: QuotesRespondItem) {
        binding.quote.text = quote.quote
        binding.author.text = quote.author
        binding.quoteContainer.setOnClickListener {
            viewModel.getQuotes()
        }

        binding.actionSave.setOnClickListener {
            ObjectAnimator.ofFloat(binding.btnSave, "scaleX", 1.0f, 0.5f).apply { duration = 400 }
                .start()
            ObjectAnimator.ofFloat(binding.txtAction, "rotationX", 0f, 90f).apply { duration = 400 }
                .start()
            viewModel.insertRoom(quote.quote, quote.author, quote.category)
            ObjectAnimator.ofFloat(binding.btnSave, "scaleX", 0.5f, 1.0f).apply { duration = 400 }
                .start()
            ObjectAnimator.ofFloat(binding.txtAction, "rotationX", 90f, 0f).apply { duration = 400 }
                .start()
        }

        binding.quoteContainer.setOnLongClickListener {
            ObjectAnimator.ofFloat(binding.quoteContainer, "scaleY", 1.0f, 0.5f).apply {
                duration = 400
                start()
            }
            val clipboard =
                requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("label", quote.quote)
            clipboard.setPrimaryClip(clip)
            Snackbar.make(requireView(), "Copied", Snackbar.LENGTH_SHORT).show()
            ObjectAnimator.ofFloat(binding.quoteContainer, "scaleY", 0.5f, 1.0f).apply {
                duration = 400
                start()
            }

            true
        }

    }


    private fun initialAnimation() {
        val a = ObjectAnimator.ofFloat(binding.quoteContainer, "alpha", 0.0f, 1.0f)
        val tx = ObjectAnimator.ofFloat(binding.quoteContainer, "translationX", 220f, 0.0f)
        val ty = ObjectAnimator.ofFloat(binding.quoteContainer, "translationY", 120.0f, 0.0f)
        val s = ObjectAnimator.ofFloat(binding.quoteContainer, "scaleX", 0.5f, 1.0f)
        val sy = ObjectAnimator.ofFloat(binding.quoteContainer, "scaleY", 0.5f, 1.0f)

        val all = AnimatorSet().apply {
            duration = 400
            playTogether(a, tx, ty, s, sy)
        }
        all.start()
    }

    private fun setNavigation() {
        binding.actionAll.setOnClickListener {
            ObjectAnimator.ofFloat(binding.btnSave, "scaleX", 1.0f, 0.5f).apply { duration = 400 }
                .start()
            findNavController().navigate(R.id.action_homeFragment_to_savedQuotesFragment)
        }
    }

}