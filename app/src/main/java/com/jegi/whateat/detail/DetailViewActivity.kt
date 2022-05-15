package com.jegi.whateat.detail

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View.VISIBLE
import com.bumptech.glide.Glide
import com.jegi.whateat.R
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
        ingredientsOne.text = intent.getStringExtra("ingredientOne")
        ingredientsTwo.text = intent.getStringExtra("ingredientTwo")
        ingredientsThree.text = intent.getStringExtra("ingredientThree")
        ingredientsOnetoex.text = intent.getStringExtra("ingredientOnetoex")
        ingredientsTwotoex.text = intent.getStringExtra("ingredientTwotoex")
        ingredientsThreetoex.text = intent.getStringExtra("ingredientThreetoex")

        if (ingredientsTwo == null){
            ingredientsTwoto.visibility = VISIBLE
        }
        if (intent.getStringExtra("ingredientThree") == null){
            ingredientsThreeto.visibility = VISIBLE
        }
        Log.d("zzzzzzzzzzzzz", "${intent.getStringExtra("ingredientTwo")}")
        Log.d("zzzzzzzzzzzzzzzzzzzzzz", "${intent.getStringExtra("ingredientThree")}")
        hyperlinkButton.setOnClickListener {
            intent.setData(Uri.parse(hy))
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(hy))
            startActivity(intent)
        }
    }
}