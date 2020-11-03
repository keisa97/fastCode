package com.keisardevs.myapplication.model

data class  User (
    val userId: String? = "",
    val userName : String? = "",
    val accountName: String = "",
    val score : Int? = 0,
    val hatClothingItemIDs : Array<String>? = null,
    val activeHatClothId : String = "",

    val shirtClothingItemIDs : Array<String>? = null,
    val ActiveShirtClothId : String = "",

    val pantsClothingItemIDs : Array<String>? = null,
    val ActivePantsClothId : String = ""



) {
    fun toMap() : Map<String, Any?> {
        return mapOf(
            "userId" to userId,
            "userName" to userName,
            "accountName" to  accountName,
            "score" to score,
            "ActiveHatClothId" to  activeHatClothId,
            "shirtClothingItemIDs" to  shirtClothingItemIDs,
            "pantsClothingItemIDs" to  pantsClothingItemIDs
        )
    }
}