package com.example.whateat

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.whateat.cookbook.CookBookFragment
import com.example.whateat.mainmenu.MainMenuFragment
import com.example.whateat.refrigerator.RefrigeratorFragment
import com.example.whateat.user.UserFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val cookBookFragment = CookBookFragment()
        val mainMenuFragment = MainMenuFragment()
        val refrigeratorFragment = RefrigeratorFragment()
        val userFragment = UserFragment()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        replaceFragment(mainMenuFragment)

 
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.bottomNavigationView_mainCook-> replaceFragment(mainMenuFragment)
                R.id.bottomNavigationView_cookBook-> replaceFragment(cookBookFragment)
                R.id.bottomNavigationView_refrigerator-> replaceFragment(refrigeratorFragment)
                R.id.bottomNavigationView_user-> replaceFragment(userFragment)
            }
            true
        }

    }

    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
                .apply {
                    replace(R.id.fragmentContainer, fragment)
                    commit()
                }
    }

}