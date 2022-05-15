package com.jegi.whateat.model

data class MainMenuEntity (
    val title: String,
    val explanation: String,
    val imageUrl: String,
    val classification: String,
    val detail: DetailEntity
)