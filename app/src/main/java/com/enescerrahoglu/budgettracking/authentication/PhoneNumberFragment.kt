package com.enescerrahoglu.budgettracking.authentication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.enescerrahoglu.budgettracking.databinding.FragmentPhoneNumberBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential


class PhoneNumberFragment : Fragment() {
    private var _binding: FragmentPhoneNumberBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: AuthenticationViewModel
    private lateinit var auth : FirebaseAuth
    lateinit var storedVerificationId:String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private var phoneNumber = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                viewModel.setLoading(false)
                activity?.let{
                    val action = VerifyFragmentDirections.actionVerifyFragmentToIndicatorActivity()
                    Navigation.findNavController(requireView()).navigate(action)
                    requireActivity().finish()
                }
            }
            override fun onVerificationFailed(e: FirebaseException) {
                viewModel.setLoading(false)
                Toast.makeText(requireContext(), e.localizedMessage, Toast.LENGTH_LONG).show()
            }
            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                viewModel.setLoading(false)
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

        viewModel = ViewModelProviders.of(this)[AuthenticationViewModel::class.java]
        observeLoadingData()

        binding.sendButton.setOnClickListener {
            sendVerificationCode(it)
        }

        binding.forgetMeCheckBox.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setForgetMe(isChecked)
            println(viewModel.forgetMe.value.toString())
        }
    }

    private fun observeLoadingData(){
        viewModel.isLoading.observe(this, Observer { data ->
            if(data == true){
                binding.progressBar.visibility = View.VISIBLE
                binding.sendButton.visibility = View.GONE
            }else{
                binding.progressBar.visibility = View.GONE
                binding.sendButton.visibility = View.VISIBLE
            }
        })
    }

    private fun sendVerificationCode(view: View){
        if(binding.phoneNumberEditText.text.toString().trim().isNotEmpty()){
            phoneNumber = "+${binding.countryCodePicker.selectedCountryCode}${binding.phoneNumberEditText.text.toString().trim()}"
            viewModel.sendVerificationCode(view, phoneNumber, auth, requireActivity(), callbacks)
        }else{
            Toast.makeText(requireContext(), "Please enter phone number!", Toast.LENGTH_LONG).show()
        }
    }
}