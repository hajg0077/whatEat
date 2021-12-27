package com.example.whateat

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.whateat.databinding.ActivityDetailBinding
import com.example.whateat.databinding.ActivityMainBinding
import com.example.whateat.mainmenu.MainMenuModel
import com.google.firebase.auth.FirebaseAuth

class DatailActivity: AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
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
