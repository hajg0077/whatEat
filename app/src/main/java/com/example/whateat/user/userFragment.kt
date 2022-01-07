package com.example.whateat.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.whateat.LoginActivity
import com.example.whateat.MainActivity
import com.example.whateat.R
import com.example.whateat.databinding.FragmentUserBinding
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class userFragment: Fragment(R.layout.fragment_user) {


    private var binding: FragmentUserBinding? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var logoutButton: Button
    private lateinit var contexts: Context
    private lateinit var userName: TextView


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentUserBinding = FragmentUserBinding.bind(view)
        binding = fragmentUserBinding

        auth = Firebase.auth

        fragmentUserBinding.logoutButton.setOnClickListener {
            auth.signOut()
            //facebook logOut
            LoginManager.getInstance().logOut()
            Log.d(TAG, "로그아웃 되었습니다.")

        }
        fragmentUserBinding.userNameTextView.setText(auth.currentUser?.displayName)

        if(auth.currentUser == null){
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }

    }




    companion object {
        private const val TAGg = "GoogleActivity"
        private const val RC_SIGN_IN = 9001

        private const val TAGf = "FacebookLogin"

        private const val TAG = "Local"
    }

}