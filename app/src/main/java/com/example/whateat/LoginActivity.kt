package com.example.whateat

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity: AppCompatActivity() {


    private lateinit var auth: FirebaseAuth

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var googleLoginButton: LoginButton

    private lateinit var facebookLoginButton: LoginButton
    private lateinit var callbackManager: CallbackManager

    private lateinit var emailText: EditText
    private lateinit var passwordText: EditText
    private lateinit var loginButton: Button
    private lateinit var signUpButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth

        // Google 로그인 구성
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        emailText = findViewById(R.id.emailEditText)
        passwordText = findViewById(R.id.passwordEditText)


        googleLoginButton = findViewById(R.id.googleLoginButton)
        googleLoginButton.setOnClickListener{
                googleSignIn()
        }

        //테스트용 로그인
        loginButton = findViewById<Button>(R.id.loginButton)
        loginButton.setOnClickListener {
            localSignIn(emailText.toString(), passwordText.toString())
        }

        signUpButton = findViewById<Button>(R.id.signUpButton)
        signUpButton.setOnClickListener {
            createLocalUser(emailText.toString(), passwordText.toString())
        }

        // Facebook 로그인 버튼 초기화
        callbackManager = CallbackManager.Factory.create()

        facebookLoginButton = findViewById(R.id.facebookLoginButton)
        facebookLoginButton.setOnClickListener{


            facebookLoginButton.setReadPermissions("email", "public_profile")
            facebookLoginButton.registerCallback(callbackManager, object :
                FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    Log.d(TAG, "facebook:onSuccess:$loginResult")
                    handleFacebookAccessToken(loginResult.accessToken)
                }

                override fun onCancel() {
                    Log.d(TAG, "facebook:onCancel")
                }

                override fun onError(error: FacebookException) {
                    Log.d(TAG, "facebook:onError", error)
                }
            })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // GoogleSignInApi.getSignInIntent(...)에서 인텐트를 실행하여 반환된 결과;
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                //Google 로그인에 성공했습니다. Firebase로 인증하세요.
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAGg, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google 로그인에 실패했습니다. UI를 적절하게 업데이트하십시오.
                Log.w(TAGg, "Google sign in failed", e)
            }
        }

        // 활동 결과를 Facebook SDK로 다시 전달
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // 로그인 성공, 로그인한 사용자 정보로 UI 업데이트
                    Log.d(TAGg, "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // 로그인에 실패하면 사용자에게 메시지를 표시합니다.
                    Log.w(TAGg, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }

    private fun googleSignIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun updateUI(user: FirebaseUser?) {
        //이곳에 사용자 정보 업데이트
    }

    companion object {
        private const val TAGg = "GoogleActivity"
        private const val RC_SIGN_IN = 9001

        private const val TAGf = "FacebookLogin"

        private const val TAG = "Local"
    }

    //로컬 아이디 생성
    private fun createLocalUser(email: String, password: String){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // 로그인 성공, 로그인한 사용자 정보로 UI 업데이트
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // 로그인에 실패하면 사용자에게 메시지를 표시합니다.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }

    private fun localSignIn(email: String, password: String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // 로그인 성공, 로그인한 사용자 정보로 UI 업데이트
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // 로그인에 실패하면 사용자에게 메시지를 표시합니다.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }



    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d(TAGf, "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // 로그인 성공, 로그인한 사용자 정보로 UI 업데이트
                    Log.d(TAGf, "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // 로그인에 실패하면 사용자에게 메시지를 표시합니다.
                    Log.w(TAGf, "signInWithCredential:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }

}