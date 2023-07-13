package com.enescerrahoglu.budgettracking.wallet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.enescerrahoglu.budgettracking.databinding.FragmentWalletBinding
import com.enescerrahoglu.budgettracking.user.UserModel
import com.enescerrahoglu.budgettracking.user.UserViewModel
import com.google.gson.Gson

class WalletFragment : Fragment() {
    private var _binding: FragmentWalletBinding? = null
    private val binding get() = _binding!!
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWalletBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]

        /*val userModelJson: String? = arguments?.getString("userModel")
        if(userModelJson.isNullOrEmpty()){
            val gson = Gson()
            val userFromJson = gson.fromJson(userModelJson, UserModel::class.java)
            binding.budgetTLTextView.text = userFromJson.monthlyBudget.toString()
        }*/
        observeData()
        binding.circularProgressBar.setProgress(66)
        binding.circularProgressBarTextView.text = "66%"
    }

    private fun observeData(){
        userViewModel.userModel.observe(this, Observer { data ->
            if(data != null){
                binding.budgetTLTextView.text = data.monthlyBudget.toString()
            }
        })
    }
}