package com.example.whateat.model

data class UserDTO(
    var uid: String? = null,
    var token: Boolean? = false,
    var ingredient: Ingredient = Ingredient()
){
    data class Ingredient(
        var ingredient: String? = null,
        var have: String? = null
    )
}
