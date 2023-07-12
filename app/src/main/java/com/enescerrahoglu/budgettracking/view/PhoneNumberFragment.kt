package com.enescerrahoglu.budgettracking.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import android.widget.Toast
import androidx.navigation.Navigation
import com.enescerrahoglu.budgettracking.databinding.FragmentPhoneNumberBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import java.util.concurrent.TimeUnit


class PhoneNumberFragment : Fragment() {
    private var _binding: FragmentPhoneNumberBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth : FirebaseAuth

    lateinit var storedVerificationId:String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    private var phoneNumber = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        auth.setLanguageCode("tr")
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                println("onVerificationCompleted")
                activity?.let{
                    val intent = Intent (it, NavigationActivity::class.java)
                    it.startActivity(intent)
                    it.finish()
                }
            }
            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(requireContext(), e.localizedMessage, Toast.LENGTH_LONG).show()
            }
            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                println(verificationId)
                storedVerificationId = verificationId
                resendToken = token

                val action = PhoneNumberFragmentDirections.actionPhoneNumberFragmentToVerifyFragment(storedVerificationId, phoneNumber)
                Navigation.findNavController(requireView()).navigate(action)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPhoneNumberBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.sendButton.setOnClickListener {
            sendVerificationCode(it)
        }
    }

    private fun sendVerificationCode(view: View){
        if(binding.phoneNumberEditText.text.toString().trim().isNotEmpty()){
            phoneNumber = "+${binding.countryCodePicker.selectedCountryCode}${binding.phoneNumberEditText.text.toString().trim()}"
            val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(requireActivity())
                .setCallbacks(callbacks)
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)
        }else{
            Toast.makeText(requireContext(), "Please enter phone number!", Toast.LENGTH_LONG).show()
        }

    }
}