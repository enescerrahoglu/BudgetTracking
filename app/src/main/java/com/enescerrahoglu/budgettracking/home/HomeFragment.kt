package com.enescerrahoglu.budgettracking.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.enescerrahoglu.budgettracking.api.CurrencyViewModel
import com.enescerrahoglu.budgettracking.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var currencyViewModel: CurrencyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currencyViewModel = ViewModelProviders.of(this)[CurrencyViewModel::class.java]
        observeCurrency()
    }

    private fun observeCurrency(){
        currencyViewModel.currencyList.observe(this, Observer { _ ->
            val tryValue = currencyViewModel.getTryValue()
            binding.currencyTextView.text = "1 USD = ${String.format("%.2f", tryValue)} TL"
        })
        //currencyViewModel.fetchCurrencyData()
    }
}