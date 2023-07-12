package com.enescerrahoglu.budgettracking.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.enescerrahoglu.budgettracking.databinding.ActivityUpdateProfileBinding

class UpdateProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.let {
            supportActionBar!!.title = "Update Profile"
        }
    }
}