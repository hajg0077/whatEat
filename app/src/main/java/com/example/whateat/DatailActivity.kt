package com.example.whateat

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.whateat.DBKey.Companion.DB_MENUS
import com.example.whateat.databinding.ActivityDetailBinding
import com.example.whateat.databinding.ActivityMainBinding
import com.example.whateat.mainmenu.MainMenuModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DatailActivity: AppCompatActivity() {

    private  val TAG = "DatailActivity"
    private lateinit var binding: ActivityDetailBinding
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val mainMainDB: DatabaseReference by lazy {
        Firebase.database.reference.child(DB_MENUS)
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        Log.d(TAG,"DatailActivity 들어옴")
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val model = intent.getParcelableExtra<MainMenuModel>("mainMenuModel")

        binding.titleTextView.text = model?.title.orEmpty()
        binding.explanationTextView.text = model?.explanation.orEmpty()
        binding.ingredients.text = model?.ingredients.orEmpty()
        binding.hyperlink.text = model?.hyperlink.orEmpty()

        Glide.with(binding.thumbnailImageView.context)
            .load(model?.imageUrl.orEmpty())
            .into(binding.thumbnailImageView)

    }
}
