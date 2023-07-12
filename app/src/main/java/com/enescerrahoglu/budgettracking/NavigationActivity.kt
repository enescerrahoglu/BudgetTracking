package com.enescerrahoglu.budgettracking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.enescerrahoglu.budgettracking.R
import com.enescerrahoglu.budgettracking.databinding.ActivityNavigationBinding
import com.enescerrahoglu.budgettracking.home.HomeFragment
import com.enescerrahoglu.budgettracking.user.ProfileFragment
import com.enescerrahoglu.budgettracking.wallet.WalletFragment

class NavigationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNavigationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
        transaction.replace(R.id.frameLayout,fragment)
        transaction.commit()
        supportActionBar?.let {
            supportActionBar!!.title = title
        }
    }
}