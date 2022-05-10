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
import com.example.whateat.databinding.ActivityLoginBinding
import com.example.whateat.databinding.ActivityMainBinding
import com.example.whateat.model.RefrigeratorDTO
import com.example.whateat.model.UserDTO
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
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.view.*


class LoginActivity: AppCompatActivity() {


    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var googleLoginButton: SignInButton

    private lateinit var facebookLoginButton: LoginButton
    private lateinit var callbackManager: CallbackManager



    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        firestore = FirebaseFirestore.getInstance()

        // Google 로그인 구성
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)


        googleLoginButton = findViewById(R.id.googleLoginButton)
        googleLoginButton.setOnClickListener{
                googleSignIn()

        }


        //테스트용 로그인
//        emailText = findViewById(R.id.emailEditText)
//        passwordText = findViewById(R.id.passwordEditText)
//
//        loginButton = findViewById<Button>(R.id.loginButton)
//        loginButton.setOnClickListener {
//            localSignIn(emailText.text.toString(), passwordText.text.toString())
//        }
//
//        signUpButton = findViewById<Button>(R.id.signUpButton)
//        signUpButton.setOnClickListener {
//            createLocalUser(emailText.text.toString(), passwordText.text.toString())
//        }


        // Facebook 로그인 버튼 초기화
        callbackManager = CallbackManager.Factory.create()

        facebookLoginButton = findViewById(R.id.facebookLoginButton)
        facebookLoginButton.setOnClickListener{

            facebookLoginButton.setReadPermissions("email")
            facebookLoginButton.registerCallback(callbackManager, object :
                FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    Log.d(TAGf, "facebook:onSuccess:$loginResult")
                    handleFacebookAccessToken(loginResult.accessToken)
                }

                override fun onCancel() {
                    Log.d(TAGf, "facebook:onCancel")
                }

                override fun onError(error: FacebookException) {
                    Log.d(TAGf, "facebook:onError", error)
                }
            })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // GoogleSignInApi.getSignInIntent(...)에서 인텐트를 실행하여 반환된 결과;
        if (requestCode == RC_SIGN_IN) {
            Log.d(TAGg, "들어옴")
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

//                val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data!!)
//
//                if (result != null) {
//                    if (result.isSuccess){
//                        Log.d("google", "result.isSuccess")
//                        val account = result.signInAccount
//                        firebaseAuthWithGoogle(account!!.idToken!!)
//                    }
//                }
            }

        //활동 결과를 Facebook SDK로 다시 전달  -- 와 1달만에 깃에서 다시 찾았다 항상 백업해두기 ********************************************************************************
        callbackManager.onActivityResult(requestCode, resultCode, data)

    }


    //구글 로그인
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // 로그인 성공, 로그인한 사용자 정보로 UI 업데이트
                    Log.d(TAGg, "구글로그인에 성공했습니다.")
                    val user = auth.currentUser
                    updateUI(user)
                    finish()
                } else {
                    // 로그인에 실패하면 사용자에게 메시지를 표시합니다.
                    Log.w(TAGg, "구글로그인에 실패했습니다.", task.exception)
                }
            }
    }
    private fun googleSignIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }


    companion object {
        private const val TAGg = "GoogleActivity"
        private const val RC_SIGN_IN = 9001

        private const val TAGf = "FacebookLogin"

        private const val TAG = "Local"
    }

//    //로컬 아이디 생성
//    private fun createLocalUser(email: String, password: String){
//        auth.createUserWithEmailAndPassword(email, password)
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    // 로그인 성공, 로그인한 사용자 정보로 UI 업데이트
//                    Log.d(TAG, "회원가입에 성공했습니다.")
//                } else {
//                    // 로그인에 실패하면 사용자에게 메시지를 표시합니다.
//                    Log.w(TAG, "회원가입에 실패했습니다.", task.exception)
//                    Toast.makeText(baseContext, "Authentication failed.",
//                        Toast.LENGTH_SHORT).show()
//                }
//            }
//    }
//
//    //로컬 로그인
//    private fun localSignIn(email: String, password: String){
//        auth.signInWithEmailAndPassword(email, password)
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    // 로그인 성공, 로그인한 사용자 정보로 UI 업데이트
//                    Log.d(TAG, "로그인에 성공했습니다.")
//                    val user = auth.currentUser
//                    updateUI(user)
//                    finish()
//                } else {
//                    // 로그인에 실패하면 사용자에게 메시지를 표시합니다.
//                    Log.w(TAG, "로그인에 실패했습니다.", task.exception)
//                    Toast.makeText(baseContext, "Authentication failed.",
//                        Toast.LENGTH_SHORT).show()
//                }
//            }
//    }


    //페이스북 로그인
    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d(TAGf, "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // 로그인 성공, 로그인한 사용자 정보로 UI 업데이트
                    Log.d(TAGf, "페이스북 로그인에 성공했습니다.")
                    val user = auth.currentUser
                    updateUI(user)
                    finish()
                } else {
                    // 로그인에 실패하면 사용자에게 메시지를 표시합니다.
                    Log.d(TAGf, "페이스북 로그인에 실패했습니다.", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    //로그인 성공시
    private fun updateUI(user: FirebaseUser?) {
        val data = hashMapOf(
            "email" to user?.email,
            "uid" to user?.uid,
        )

        val loginData = hashMapOf(
            "token" to true,
        )

        firestore.collection("User").document("${user?.uid}")
            .set(data, SetOptions.merge())

        var tokenMap = mutableMapOf<String, Any>()
        tokenMap["token"] = false

        firestore.collection("User").document("${user?.uid}").get()
            .addOnSuccessListener { documentSnapshot ->
                with(documentSnapshot){
                    //var count = 0
                    val a = getBoolean("token")
                    if (a == null){
                        firestore.collection("User").document("${user?.uid}")
                            .set(tokenMap, SetOptions.merge())

                        firestore.collection("Ingredient")
                            .addSnapshotListener { querySnapshot, _ ->
                                if (querySnapshot == null) return@addSnapshotListener

                                for (snapshot in querySnapshot.documents) {
                                    val item = snapshot.toObject(RefrigeratorDTO::class.java)
                                    if (item != null) {
                                        firestore.collection("User").document("${user?.uid}")
                                            .collection("ingredient").document("${item.ingredientName}")
                                            .set(item)
                                            //count++
                                            //Log.d("카운터","$count")
                                        }

                                }
                                firestore.collection("User").document("${user?.uid}").set(loginData,
                                    SetOptions.merge())
                            }
                    }
                }
            }
    }

}