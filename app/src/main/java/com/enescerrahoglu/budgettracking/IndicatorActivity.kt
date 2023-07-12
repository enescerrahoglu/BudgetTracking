package com.enescerrahoglu.budgettracking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.enescerrahoglu.budgettracking.authentication.AuthenticationViewModel
import com.enescerrahoglu.budgettracking.databinding.ActivityIndicatorBinding
import com.enescerrahoglu.budgettracking.user.UserModel
import com.enescerrahoglu.budgettracking.user.UserViewModel
import com.google.firebase.Timestamp

class IndicatorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIndicatorBinding
    private lateinit var viewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIndicatorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.let {
            supportActionBar!!.hide()
        }

        val intent = Intent(this, NavigationActivity::class.java)
        startActivity(intent)
        finish()

        viewModel = ViewModelProvider(this)[UserViewModel::class.java]
        val user = viewModel.getUser()
        println(viewModel.a.toString())

        observeData()

        println(viewModel.getUser()?.phoneNumber)
        viewModel.userModel.value?.let {
            println(viewModel.userModel.value!!.phoneNumber)
            viewModel.hasUser(it.phoneNumber){ hasUser ->
                println(hasUser.toString())
                if(!hasUser){
                    viewModel.createUser()
                }
            }
        }

    }

    fun observeData(){
        viewModel.userModel.observe(this, Observer { data ->
            println(data?.phoneNumber)
        })
    }
}