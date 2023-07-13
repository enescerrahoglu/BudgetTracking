package com.enescerrahoglu.budgettracking.user

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.enescerrahoglu.budgettracking.databinding.FragmentProfileBinding
import com.google.gson.Gson

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]

        observeData()
    }

    private fun observeData(){
        userViewModel.userModel.observe(this, Observer { data ->
            if(data != null){
                binding.currentUserPhoneNumberTextView.text = "${data.firstName} ${data.lastName}"
                binding.currentUserPhoneNumberLL.setOnClickListener {
                    activity?.let{
                        val intent = Intent (it, UpdateProfileActivity::class.java)
                        val gson = Gson()
                        val json = gson.toJson(data)
                        intent.putExtra("userModel", json)
                        it.startActivity(intent)
                    }
                }
            }
        })
    }
}