package com.example.whateat.model

data class UserDTO(
    var uid: String? = null,
    var token: Boolean? = false,
    var email: String? = null
){
    data class Ingredient(
        var ingredientName: String? = null,
        var have: Boolean? = false
    )
}
