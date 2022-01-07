package com.example.whateat.mainmenu

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class MainMenuModel (
        val title: String,
        val ingredients: String,
        val imageUrl: String,
        val explanation: String,
        val hyperlink: String,
        val ingredientOne: String,
        val ingredientTwo: String,
        val ingredientThree: String

        ): Parcelable{
                constructor(): this("","","","","","","","")
        }