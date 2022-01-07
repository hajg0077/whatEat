package com.example.whateat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.compose.ui.state.ToggleableState
import androidx.fragment.app.Fragment
import com.example.whateat.cookbook.CookBookFragment
import com.example.whateat.databinding.ActivityMainBinding
import com.example.whateat.mainmenu.MainMenuFragment
import com.example.whateat.refrigerator.RefrigeratorFragment
import com.example.whateat.user.userFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var userDB: DatabaseReference

    override fun onStart() {
        super.onStart()
        if (auth.currentUser == null){
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val cookBookFragment = CookBookFragment()
        val mainMenuFragment = MainMenuFragment()
        val refrigeratorFragment = RefrigeratorFragment()

        //이 부분 수정 요망!
        val userFragment = userFragment()

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

    // 이것으로 어뎁터 연결
    fun initMainMenu(){
        //val adapter = MainMenuAdapter()
        //binding.fragmentContainer.layoutManager = LinearLayoutManager(this)
        //binding.fragmentContainer.adapter = adapter
    }



}