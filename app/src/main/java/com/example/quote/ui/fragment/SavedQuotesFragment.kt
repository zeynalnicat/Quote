package com.example.quote.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.quote.R
import com.example.quote.data.entity.QuoteRoomEntity
import com.example.quote.databinding.FragmentSavedQuotesBinding
import com.example.quote.ui.SavedQuotesAdapter
import com.example.quote.ui.viewmodel.SavedQuotesViewModel
import com.example.quote.util.Resource
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
        val adapter = SavedQuotesAdapter()
        adapter.submitList(items)
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 1)
        binding.recyclerView.adapter = adapter
    }

    private fun setNavigation() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }


}