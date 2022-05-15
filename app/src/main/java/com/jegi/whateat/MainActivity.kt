package com.jegi.whateat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.jegi.whateat.cookbook.CookBookFragment
import com.jegi.whateat.databinding.ActivityMainBinding
import com.jegi.whateat.mainmenu.MainMenusFragment
import com.jegi.whateat.refrigerator.RefrigeratorFragment
import com.jegi.whateat.user.userFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference


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

        //facebook 해시키
        //Vd/r3/Qoh9cHeJd0dNLWnmDMSPA=
        //printHashKey()


        val cookBookFragment = CookBookFragment()

        //수정중
        val mainMenuFragment = MainMenusFragment()
        val refrigeratorFragment = RefrigeratorFragment()
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

        //facebook 해시키키
//     fun printHashKey() {
//        try {
//            val info: PackageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
//            for (signature in info.signatures) {
//                val md: MessageDigest = MessageDigest.getInstance("SHA")
//                md.update(signature.toByteArray())
//                val hashKey: String = String(Base64.encode(md.digest(), 0))//Android.util의 Base64를 import 해주시면 됩니다.
//                Log.i("key", "printHashKey() Hash Key: $hashKey")
//            }
//        } catch (e: NoSuchAlgorithmException) {
//            Log.e("key", "printHashKey()", e)
//        } catch (e: Exception) {
//            Log.e("key", "printHashKey()", e)
//        }
//    }

}