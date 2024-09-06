package com.example.quote.ui.fragment

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.quote.R
import com.example.quote.data.entity.QuoteRoomEntity
import com.example.quote.databinding.FragmentSavedQuotesBinding
import com.example.quote.databinding.LayoutPopupBinding
import com.example.quote.ui.adapter.SavedQuotesAdapter
import com.example.quote.ui.viewmodel.SavedQuotesViewModel
import com.example.quote.util.Resource
import com.example.quote.util.Util
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SavedQuotesFragment : Fragment() {
    private lateinit var binding: FragmentSavedQuotesBinding

    private val viewModel: SavedQuotesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedQuotesBinding.inflate(inflater, container, false)
        setNavigation()
        setAnimation()
        viewModel.getAll()
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.quotes.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    setAdapter(it.data)
                }

                is Resource.Error -> {
                    Snackbar.make(requireView(), it.exception.message!!, Snackbar.LENGTH_SHORT)
                        .setAction("Retry") {
                            viewModel.getAll()
                        }
                }

                else -> {

                }
            }

        }
    }

    private fun setAdapter(items: List<QuoteRoomEntity>) {
        val adapter = SavedQuotesAdapter({ content, view -> cp(content, view) },
            { quote -> openPopUp(quote) })
        adapter.submitList(items)
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 1)
        binding.recyclerView.adapter = adapter
    }

    private fun setNavigation() {
        binding.btnBack.setOnClickListener {
            ObjectAnimator.ofFloat(binding.btnBack, "scaleX", 1.0f, 0.5f).apply { duration = 400 }
                .start()
            findNavController().popBackStack()
        }
    }

    private fun setAnimation() {
        val tx = ObjectAnimator.ofFloat(binding.recyclerView, "translationX", -220f, 0.0f)
        val ty = ObjectAnimator.ofFloat(binding.recyclerView, "translationY", 220f, 0.0f)

        val a = AnimatorSet().apply {
            duration = 500
            playTogether(tx, ty)
        }

        a.start()
    }


    fun cp(content: String, view: View) {
        val util = Util(requireContext(), requireView())
        util.copyClipboard(view, content)
    }

    fun openPopUp(quote:QuoteRoomEntity) {
        val builder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
        val view = layoutInflater.inflate(R.layout.layout_popup, null)

        view.findViewById<TextView>(R.id.quote).text = quote.content
        view.findViewById<TextView>(R.id.author).text = quote.author
        view.findViewById<TextView>(R.id.txtCategory).text = quote.category

        val button = view.findViewById<Button>(R.id.btnRemove)
        val dialog = builder.create()
        dialog.setView(view)
        button.setOnClickListener {
            viewModel.remove(quote)
            dialog.cancel()
        }



        dialog.setCanceledOnTouchOutside(true)
        dialog.show()

        val window = dialog.window
        if (window != null) {
            val layoutParams = window.attributes
            layoutParams.dimAmount = 0.5f
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            window.attributes = layoutParams
        }
    }


}