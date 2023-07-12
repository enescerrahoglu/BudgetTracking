package com.enescerrahoglu.budgettracking.authentication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.enescerrahoglu.budgettracking.databinding.FragmentVerifyBinding
import com.enescerrahoglu.budgettracking.user.UserModel
import com.enescerrahoglu.budgettracking.user.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

class VerifyFragment : Fragment() {
    private var _binding: FragmentVerifyBinding? = null
    private lateinit var authenticationViewModel: AuthenticationViewModel
    private lateinit var userViewModel: UserViewModel
    private val binding get() = _binding!!

    private var storedVerificationId = ""
    private var phoneNumber = ""
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVerifyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            storedVerificationId = VerifyFragmentArgs.fromBundle(it).storedVerificationId
            phoneNumber = VerifyFragmentArgs.fromBundle(it).phoneNumber
        }

        authenticationViewModel = ViewModelProviders.of(this)[AuthenticationViewModel::class.java]
        //userViewModel = ViewModelProviders.of(this)[UserViewModel::class.java]
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        userViewModel.a = 10
        println(userViewModel.a.toString())
        observeLoadingData()

        binding.verifyButton.setOnClickListener {
            var enteredCode = binding.verificationCodeEditText.text.toString().trim()
            if(enteredCode.isNotEmpty()){
                authenticationViewModel.setLoading(true)

                var userModel = UserModel(phoneNumber, null, null, null)
                userViewModel.setUser(userModel)

                val credential : PhoneAuthCredential = PhoneAuthProvider.getCredential(storedVerificationId, enteredCode)
                authenticationViewModel.signInWithPhoneAuthCredential(credential, auth, requireActivity(), requireView(), requireContext(), phoneNumber)
            }else{
                Toast.makeText(requireContext(),"Enter OTP",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeLoadingData(){
        authenticationViewModel.isLoading.observe(this, Observer { data ->
            if(data == true){
                binding.progressBar.visibility = View.VISIBLE
                binding.verifyButton.visibility = View.GONE
            }else{
                binding.progressBar.visibility = View.GONE
                binding.verifyButton.visibility = View.VISIBLE
            }
        })
    }
}