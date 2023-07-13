package com.enescerrahoglu.budgettracking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.enescerrahoglu.budgettracking.databinding.ActivityNavigationBinding
import com.enescerrahoglu.budgettracking.home.HomeFragment
import com.enescerrahoglu.budgettracking.user.ProfileFragment
import com.enescerrahoglu.budgettracking.user.UserModel
import com.enescerrahoglu.budgettracking.user.UserViewModel
import com.enescerrahoglu.budgettracking.wallet.WalletFragment
import com.google.gson.Gson

class NavigationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNavigationBinding
    private lateinit var viewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProviders.of(this)[UserViewModel::class.java]
        val userExtra = intent.getStringExtra("userModel")
        val gson = Gson()
        val userFromJson = gson.fromJson(userExtra, UserModel::class.java)
        viewModel.setUser(userFromJson)

        println("Welcome ${viewModel.userModel.value?.firstName} ${viewModel.userModel.value?.lastName}")

        loadFragment(HomeFragment(), "Home")
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    if (binding.bottomNavigation.selectedItemId == R.id.home) {
                        false
                    } else {
                        loadFragment(HomeFragment(), "Home")
                        true
                    }
                }
                R.id.wallet -> {
                    if (binding.bottomNavigation.selectedItemId == R.id.wallet) {
                        false
                    } else {
                        loadFragment(WalletFragment(), "Wallet")
                        true
                    }
                }
                R.id.profile -> {
                    if (binding.bottomNavigation.selectedItemId == R.id.profile) {
                        false
                    } else {
                        loadFragment(ProfileFragment(), "Profile")
                        true
                    }
                }
                else -> {
                    loadFragment(HomeFragment(),"Home")
                    true
                }
            }
        }
    }
    private  fun loadFragment(fragment: Fragment, title: String){
        val transaction = supportFragmentManager.beginTransaction()
        /*viewModel.userModel.value?.let {
            val bundle = Bundle()
            val gson = Gson()
            val json = gson.toJson(viewModel.userModel.value)
            bundle.putString("userModel", json)
            fragment.arguments = bundle
            println(viewModel.userModel.value?.phoneNumber)
        }*/
        transaction.replace(R.id.frameLayout,fragment)
        transaction.commit()
        supportActionBar?.let {
            supportActionBar!!.title = title
        }
    }
}