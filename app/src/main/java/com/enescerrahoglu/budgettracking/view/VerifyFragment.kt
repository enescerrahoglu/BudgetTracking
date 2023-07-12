package com.enescerrahoglu.budgettracking.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import com.enescerrahoglu.budgettracking.databinding.FragmentVerifyBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

class VerifyFragment : Fragment() {
    private var _binding: FragmentVerifyBinding? = null
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

        binding.verifyButton.setOnClickListener {
            var enteredCode = binding.verificationCodeEditText.text.toString().trim()
            if(enteredCode.isNotEmpty()){
                val credential : PhoneAuthCredential = PhoneAuthProvider.getCredential(storedVerificationId, enteredCode)
                signInWithPhoneAuthCredential(credential, activity)
            }else{
                Toast.makeText(requireContext(),"Enter OTP",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential, activity: FragmentActivity?) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    activity?.let{
                        val action = VerifyFragmentDirections.actionVerifyFragmentToNavigationActivity()
                        Navigation.findNavController(requireView()).navigate(action)
                        requireActivity().finish()
                    }
                } else {
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(requireContext(),"Invalid OTP",Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }
}