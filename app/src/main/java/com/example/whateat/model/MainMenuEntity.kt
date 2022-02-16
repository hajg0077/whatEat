package com.example.whateat.model

import android.os.Parcelable

data class MainMenuEntity (
    val title: String,
    val explanation: String,
    val imageUrl: String,
    val classification: String,
    val detail: DetailEntity
)