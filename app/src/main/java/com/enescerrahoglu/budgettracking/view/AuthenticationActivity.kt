package com.enescerrahoglu.budgettracking.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.enescerrahoglu.budgettracking.databinding.ActivityAuthenticationBinding

class AuthenticationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthenticationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}