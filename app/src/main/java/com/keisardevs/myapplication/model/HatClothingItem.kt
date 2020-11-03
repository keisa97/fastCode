package com.keisardevs.myapplication.model

data class HatClothingItem (
    val clothingItemImageUrl: String? = "",
    val clothingItemID : String? = "",
    val clothingItemName: String? = "",
    val scoreValue : Int? = 0,
    val itemTagID : String? = ""
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as HatClothingItem

        if (clothingItemImageUrl != other.clothingItemImageUrl) return false
        if (clothingItemID != other.clothingItemID) return false
        if (clothingItemName != other.clothingItemName) return false
        if (scoreValue != other.scoreValue) return false
        if (itemTagID != other.itemTagID) return false

        return true
    }

    override fun hashCode(): Int {
        var result = clothingItemImageUrl?.hashCode() ?: 0
        result = 31 * result + (clothingItemID?.hashCode() ?: 0)
        result = 31 * result + (clothingItemName?.hashCode() ?: 0)
        result = 31 * result + (scoreValue ?: 0)
        result = 31 * result + (itemTagID?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "HatClothingItem(clothingItemImageUrl=$clothingItemImageUrl, clothingItemID=$clothingItemID, clothingItemName=$clothingItemName, scoreValue=$scoreValue, itemTagID=$itemTagID)"
    }
}
