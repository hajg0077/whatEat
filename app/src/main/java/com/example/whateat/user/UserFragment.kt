package com.example.whateat.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.example.whateat.R
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class UserFragment: Fragment(R.layout.fragment_user) {

    private lateinit var auth: FirebaseAuth
    private lateinit var callbackManager: CallbackManager
    private lateinit var facebookLoginButton: LoginButton
    private lateinit var contexts: Context
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var signUpButton: Button
    private val TAG = "UserFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user, container, false)
        if (container != null) {
            contexts = container.getContext()
        }
        facebookLoginButton = view.findViewById(R.id.facebookLoginButton)
        emailEditText = view.findViewById(R.id.emailEditText)
        passwordEditText = view.findViewById(R.id.passwordEditText)
        loginButton = view.findViewById(R.id.loginButton)
        signUpButton = view.findViewById(R.id.signUpButton)



        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        auth = Firebase.auth
        callbackManager = CallbackManager.Factory.create()
        Log.d(TAG, "로그인 프래그먼트 들어옴")

        initLoginButton()
        initSignUpButton()
        initEmailAndPasswordEditText()
        initFacebookLoginButton()
    }

    private fun initEmailAndPasswordEditText() {
        val emailText = emailEditText
        val passwordText = passwordEditText

        emailText.addTextChangedListener {
            val enable = emailEditText.text.isNotEmpty() && passwordText.text.isNotEmpty()
            signUpButton.isEnabled = enable
            loginButton.isEnabled = enable
        }

        passwordText.addTextChangedListener {
            val enable = emailEditText.text.isNotEmpty() && passwordText.text.isNotEmpty()
            signUpButton.isEnabled = enable
            loginButton.isEnabled = enable
        }
    }

    private fun initSignUpButton() {
        val signUp = signUpButton
        Log.d(TAG,"회원가입 버튼 생성")
        signUp.setOnClickListener {
            val email = getInputEmail()
            val password = getInputPassword()
            Log.d(TAG,"회원가입 버튼 눌림")
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        Toast.makeText(context,"회원가입에 성공했습니다.",Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context,"회원가입에 실패했습니다.",Toast.LENGTH_SHORT).show()

                    }
                }

        }
    }

    private fun initLoginButton() {
        val login = loginButton
        Log.d(TAG,"로그인 버튼 생성")
        login.setOnClickListener {
            val email = getInputEmail()
            val password = getInputPassword()
            Log.d(TAG,"로그인 버튼 눌림")
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        activity?.supportFragmentManager        //액티비티에 finish
                            ?.beginTransaction()
                            ?.commit()
                    } else {
                        Toast.makeText(context,"이메일 또는 비밀번호를 확인해주세요.",Toast.LENGTH_SHORT).show()
                    }

                }

        }
    }

    private fun getInputEmail(): String{
        return emailEditText.text.toString()
    }

    private fun getInputPassword(): String{
        return passwordEditText.text.toString()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }




    private fun initFacebookLoginButton(){
        Log.d(TAG,"페이스북 로그인 버튼 눌림")
        facebookLoginButton.setPermissions("email", "public_profile")
        facebookLoginButton.registerCallback(callbackManager, object: FacebookCallback<LoginResult>{
            override fun onSuccess(result: LoginResult) {
                //로그인 성공
                val credential = FacebookAuthProvider.getCredential(result.accessToken.token)
                auth.signInWithCredential(credential)
                    .addOnCompleteListener { task->
                            if (task.isSuccessful){
                                activity?.supportFragmentManager
                                    ?.beginTransaction()
                                    ?.commit()
                                Toast.makeText(context,"페이스북 로그인에 성공하셨습니다.",Toast.LENGTH_SHORT).show()

                            } else {
                                Toast.makeText(context,"페이스북 로그인에 실패하셨습니다.",Toast.LENGTH_SHORT).show()
                            }
                    }
            }

            override fun onCancel() {
                //TODO("Not yet implemented")
            }

            override fun onError(error: FacebookException?) {
                Toast.makeText(context,"페이스북 로그인에 실패하셨습니다.",Toast.LENGTH_SHORT).show()
            }

        })
    }







}