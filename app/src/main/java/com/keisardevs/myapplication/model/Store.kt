package com.keisardevs.myapplication.model

data class Store(
    val storeName: String = "",
    val storeId: String = "",
    val storeImageUrl: String= "",
    val storeLocationLatitude: String= "",
    val storeLocationLongitude: String= ""
    //, val availableClothingItems : Array<String>?
) {


    override fun toString(): String {
        return "Store(storeName='$storeName', storeId='$storeId', storeImageUrl='$storeImageUrl', storeLocationLatitude=$storeLocationLatitude, storeLocationLongitude=$storeLocationLongitude)"
    }
}