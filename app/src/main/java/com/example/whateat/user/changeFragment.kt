package com.example.whateat.user

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.whateat.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class changeFragment: Fragment(R.layout.fragment_change) {

    private lateinit var auth: FirebaseAuth
    private lateinit var logoutButton: Button
    private lateinit var contexts: Context
    private lateinit var userName: TextView


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth

        logoutButton.setOnClickListener {
            Firebase.auth.signOut()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_change, container, false)
        if (container != null) {
            contexts = container.getContext()
        }
        logoutButton = view.findViewById(R.id.logoutButton)
        userName = view.findViewById(R.id.userNameTextView)

        return super.onCreateView(inflater, container, savedInstanceState)
    }


}