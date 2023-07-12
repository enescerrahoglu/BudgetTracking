package com.enescerrahoglu.budgettracking.authentication

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

class AuthenticationViewModel : ViewModel() {
    val isLoading = MutableLiveData<Boolean>()
    val forgetMe = MutableLiveData<Boolean>()

    fun setLoading(loadingStatus : Boolean){
        isLoading.value = loadingStatus
    }

    fun setForgetMe(forgetStatus : Boolean){
        forgetMe.value = forgetStatus
    }

    fun sendVerificationCode(view: View, phoneNumber: String, auth: FirebaseAuth, activity: Activity, callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks){
        isLoading.value = true
        try {
            val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(activity)
                .setCallbacks(callbacks)
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)
        }catch (exception: Exception){
            isLoading.value = false
            Toast.makeText(view.context, exception.localizedMessage?.toString() ?: "Error!" , Toast.LENGTH_LONG).show()
        }
    }

    fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential, auth: FirebaseAuth, activity: Activity, view: View, context:Context) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    val action = VerifyFragmentDirections.actionVerifyFragmentToIndicatorActivity()
                    Navigation.findNavController(view).navigate(action)
                    activity.finish()
                } else {
                    isLoading.value = false
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(context,"Invalid OTP", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }
}