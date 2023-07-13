package com.enescerrahoglu.budgettracking

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import com.enescerrahoglu.budgettracking.authentication.AuthenticationActivity
import com.enescerrahoglu.budgettracking.databinding.ActivityIndicatorBinding
import com.enescerrahoglu.budgettracking.user.UpdateProfileActivity
import com.enescerrahoglu.budgettracking.user.UserModel
import com.enescerrahoglu.budgettracking.user.UserViewModel
import com.google.gson.Gson


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

        val preference: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        //val preference = getSharedPreferences(resources.getString(R.string.app_name), Context.MODE_PRIVATE)
        val forgetMe = preference.getBoolean("forgetMe", false)
        val userSPs = preference.getString("userModel", null)
        println("forgetMe"+forgetMe.toString())
        println("userSPs"+ userSPs.toString())

        if(forgetMe){
            val intent = Intent(this, AuthenticationActivity::class.java)
            startActivity(intent)
            finish()
        }else{
            val userExtra = intent.getStringExtra("userModel")
            val userSP = preference.getString("userModel", null)
            if(userExtra != null || !userSP.isNullOrEmpty()){
                val gson = Gson()
                var userModel : UserModel? = null
                if(userExtra == null){
                    userModel = gson.fromJson(userSP, UserModel::class.java)
                }else{
                    userModel = gson.fromJson(userExtra, UserModel::class.java)
                }

                viewModel = ViewModelProviders.of(this)[UserViewModel::class.java]
                viewModel.setUser(userModel)
                viewModel.userModel.value?.let {
                    println("Phone: " + viewModel.userModel.value!!.phoneNumber)
                    viewModel.hasUser(it.phoneNumber!!){ hasUser ->
                        println("hasUser: $hasUser")
                        if(!hasUser){
                            viewModel.createUser{ status ->
                                if(status){
                                    val intent = Intent(this, UpdateProfileActivity::class.java)
                                    val user = UserModel(viewModel.userModel.value!!.phoneNumber, null, null, 0.0)
                                    val gson = Gson()
                                    val json = gson.toJson(user)
                                    intent.putExtra("userModel", json)
                                    startActivity(intent)
                                    finish()
                                }
                            }
                        }else{

                            val intent = Intent(this, NavigationActivity::class.java)
                            var user = UserModel(viewModel.userModel.value!!.phoneNumber, null, null, 0.0)
                            viewModel.getUser(user.phoneNumber){ userModel ->
                                if(userModel != null){
                                    user = userModel
                                    viewModel.setUser(user)
                                    val gson = Gson()
                                    val json = gson.toJson(user)

                                    intent.putExtra("userModel", json)
                                    startActivity(intent)
                                    finish()
                                }
                            }

                        }
                    }
                }
            }else{
                val intent = Intent(this, AuthenticationActivity::class.java)
                startActivity(intent)
                finish()
            }

        }
    }
}