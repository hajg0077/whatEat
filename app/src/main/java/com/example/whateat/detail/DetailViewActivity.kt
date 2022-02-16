package com.example.whateat.detail

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.example.whateat.R
import com.example.whateat.model.MenuDTO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_detail_view.*

class DetailViewActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_view)

        val fi = FirebaseFirestore.getInstance()
        val hy = intent.getStringExtra("hyperlink")


        titleTextView.text = intent.getStringExtra("title")
        explanationTextView.text = intent.getStringExtra("explanation")
        Glide.with(thumbnailImageView).load(intent.getStringExtra("imageUrl")).into(thumbnailImageView)
        ingredients.text = intent.getStringExtra("ingredients")
        hyperlink.text = hy



    }
}