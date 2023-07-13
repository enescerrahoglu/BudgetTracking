package com.enescerrahoglu.budgettracking.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.enescerrahoglu.budgettracking.NavigationActivity
import com.enescerrahoglu.budgettracking.databinding.ActivityUpdateProfileBinding
import com.google.gson.Gson

class UpdateProfileActivity : AppCompatActivity() {
    private lateinit var viewModel: UserViewModel

    private lateinit var binding: ActivityUpdateProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProviders.of(this)[UserViewModel::class.java]

        supportActionBar?.let {
            supportActionBar!!.title = "Update Profile"
        }
        val userExtra = intent.getStringExtra("userModel")
        val gson = Gson()
        val userFromJson = gson.fromJson(userExtra, UserModel::class.java)
        viewModel.setUser(userFromJson)
        binding.phoneNumberEditText.setText(userFromJson.phoneNumber)
        binding.firstNameEditText.setText(userFromJson.firstName)
        binding.lastNameEditText.setText(userFromJson.lastName)
        userFromJson.monthlyBudget?.let {
            binding.monthlyBudgetEditText.setText(userFromJson.monthlyBudget!!.toString())
        }
        binding.updateProfileButton.setOnClickListener {
            updateUser()
        }
    }

    private fun updateUser(){
        val firstName = binding.firstNameEditText.text.toString()
        val lastName =binding.lastNameEditText.text.toString()
        val monthlyBudget = binding.monthlyBudgetEditText.text.toString()
        if(firstName.isNotEmpty()  && lastName.isNotEmpty() && monthlyBudget.isNotEmpty() ){
            viewModel.userModel.value?.let {
                val user = UserModel(viewModel.userModel.value!!.phoneNumber,firstName, lastName, monthlyBudget.toDouble())
                viewModel.setUser(
                    user
                )
                viewModel.updateUser { isSuccessful ->
                    if(isSuccessful){
                        val intent = Intent(this, NavigationActivity::class.java)
                        val user = viewModel.userModel.value
                        val gson = Gson()
                        val json = gson.toJson(user)
                        intent.putExtra("userModel", json)
                        startActivity(intent)
                        finish()
                        println("isSuccessful")
                    }
                }
            }
        }else{
            println("false")
        }
    }
}